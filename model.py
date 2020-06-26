import torch
import torch.nn as nn
import torch.nn.functional as F

from const import num_params, length_params, total_inputs


class Model(nn.Module):
    def __init__(self):
        super(Model, self).__init__()

        # size = (length_params, num_params // length_params)
        size = total_inputs
        # kernel_size = num_params * 2 + 1
        # padding = kernel_size // 2
        #
        # self.cnn1 = nn.Conv1d(1, 10, kernel_size, padding=padding)
        # size *= 10
        # self.max_pool_1 = nn.MaxPool1d(2)
        # size //= 2
        # self.cnn2 = nn.Conv1d(10, 20, kernel_size, padding=padding)
        # size *= 2
        # self.max_pool_2 = nn.MaxPool1d(4)
        # size //= 4
        # # self.cnn1 = nn.Conv2d(1, )
        #
        self.into_linear_size = size

        layer_sizes = [self.into_linear_size, 100, 100, 40, 20, 2]

        layers = []
        for i in range(len(layer_sizes) - 1):
            layers.append(nn.Linear(layer_sizes[i], layer_sizes[i + 1]))
            layers.append(nn.Dropout(0.02))
            layers.append(nn.BatchNorm1d(layer_sizes[i + 1]))

        layers.pop()

        self.layers = layers
        self.softmax = nn.Softmax(dim=1)

        for i in range(len(layers)):
            setattr(self, f"layer-{i}", layers[i])

    def forward(self, x):
        # x = self.max_pool_1(self.cnn1(x))
        # x = self.max_pool_2(self.cnn2(x))
        #
        x = x.view(-1, self.into_linear_size)

        for layer in self.layers[:-1]:
            x = F.relu(layer(x))

        x = self.layers[-1](x)

        if not self.training:
            x = self.softmax(x)

        return x

    @staticmethod
    def Load(path):
        model = Model()
        model.load_state_dict(torch.load(path))
        model.eval()
        return model

    def save(self, path):
        torch.save(self.state_dict(), path)
