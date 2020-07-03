import torch
import torch.nn as nn
import torch.nn.functional as F

from const import num_params, length_params, total_inputs, device, line_length


def generate_layers(layer_sizes):
    layers = []
    for i in range(len(layer_sizes) - 1):
        layers.append(nn.Linear(layer_sizes[i], layer_sizes[i + 1]))
        # layers.append(nn.Dropout(0.02))
        layers.append(nn.BatchNorm1d(layer_sizes[i + 1]))

    # layers.pop()
    layers.pop()

    return layers


def cnn_output_size(input_size, kernel_size, padding, stride=1):
    from itertools import cycle
    output = []

    for input, kernel, p in zip(input_size, kernel_size, padding):
        dim_size = (input - kernel + 2 * p) // stride + 1
        output.append(dim_size)

    return tuple(output)


def generate_cnn_layers(input_size, channels_in, channels_out, kernels):
    # returns a function which does the cnn operation as well as the output width and height
    # and the cnn layers
    from math import ceil
    from functools import reduce

    original_input_size = input_size
    work_layers = []
    max_pool_size = 2

    for kernel, ch_out in zip(kernels, channels_out):
        padding = []

        print(input_size, kernel)

        for in_size, kernel_size in zip(input_size, kernel):
            padding.append(ceil(kernel_size / 2))

        padding = tuple(padding)
        cnn = nn.Conv2d(channels_in, ch_out, kernel_size=kernel, stride=1, padding=padding)
        max_pool = nn.MaxPool2d(kernel_size=max_pool_size)

        work_layers = work_layers + [cnn, max_pool]

        input_size = cnn_output_size(input_size, kernel, padding)
        input_size = tuple(size // max_pool_size for size in input_size)
        channels_in = ch_out

    print(original_input_size)
    input_shape = (-1, 1, original_input_size[0], original_input_size[1])
    print(input_size, channels_in)
    total_output_size = reduce(lambda x, y: x*y, input_size) * channels_in

    def cnn(x):
        x = x.view(input_shape)

        for layer in work_layers:
            x = layer(x)

        x = x.view(-1, total_output_size)

        return x

    return work_layers, total_output_size, cnn


class Model(nn.Module):
    def __init__(self):
        super(Model, self).__init__()

        # size = (length_params, num_params // length_params)
        size = total_inputs
        layers = []

        self.input_size = size
        #
        # self.cnn_height = length_params
        # self.cnn_width = 10
        # self.into_cnn_size = self.cnn_height * self.cnn_width
        #
        # print("into pre-process:", size)
        # layers += self.pre_process = generate_layers([size, size // 2, self.into_cnn_size])
        #
        # size = self.into_cnn_size
        into_cnn_shape = (size // length_params, length_params)

        print("into cnn:", size)
        cnn_layers, size, self.cnn1 = generate_cnn_layers(into_cnn_shape, 1, [10, 20, 30], [(2, length_params)]*3)

        layers += cnn_layers

        print("into last layers:", size)
        self.last_layers = generate_layers([size, 20, 2])

        layers += self.last_layers

        self.softmax = nn.Softmax(dim=1)

        for i in range(len(layers)):
            setattr(self, f"layer-{i}", layers[i])

    def forward(self, x):
        # x = self.max_pool_1(self.cnn1(x))
        # x = self.max_pool_2(self.cnn2(x))
        #
        x = x.view(-1, self.input_size)

        # print(x.size())
        # for layer in self.pre_process:
        #     x = F.relu(layer(x))

        # print(x.size())

        x = self.cnn1(x)
        # print(x.size())

        for layer in self.last_layers[:-1]:
            x = F.relu(layer(x))

        x = self.last_layers[-1](x)

        if not self.training:
            # x = self.softmax(x)
            pass

        return x

    @staticmethod
    def Load(path):
        model = Model()
        model.load_state_dict(torch.load(path, map_location=device))
        model.eval()
        return model

    def save(self, path):
        torch.save(self.state_dict(), path)
