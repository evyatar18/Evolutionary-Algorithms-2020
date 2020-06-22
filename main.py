import torch
import torch.utils.data as data
import torch.nn as nn
import torch.nn.functional as F
import numpy

import functools

num_params = 10 * 4


def get_data(file_name, only_input=False, max_len=0):
    ret = []
    i = 0
    with open(file_name, "r") as f:
        for point in f:
            i += 1
            point.replace(" ", "")
            params = point.split(",")
            assert len(params) == 121, "wrong param number when reading data! Is currently " + str(len(params))
            data = list(map(float, params[1 + (120 - num_params):]))
            # data = (data - data.min()) / (data.max() - data.min())

            avg_0 = numpy.average(data[::2])
            std_0 = numpy.std(data[::2])

            data = [data[i] - 100 if i % 2 else (data[i] - avg_0) / std_0 for i in range(len(data))]
            data = list(map(float, data))

            if not only_input:
                label = int(params[0])
                ret.append((torch.tensor(data), label))
            else:
                ret.append(data)

            if i == max_len:
                break
    return ret


class Model(nn.Module):
    def __init__(self):
        super(Model, self).__init__()

        size = num_params
        # self.cnn1 = nn.Conv1d(1, 10, 4 * 2)
        # size *= 10
        # self.max_pool_1 = nn.MaxPool1d(2)
        # size //= 2
        # self.cnn2 = nn.Conv1d(10, 20, 4 * 2)
        # size *= 2
        # self.max_pool_2 = nn.MaxPool1d(4)
        # size //= 4

        layers = [
            nn.Linear(size, 50),
            # nn.Dropout(p=0.01),
            # nn.BatchNorm1d(100),
            # nn.Linear(100, 50),
            nn.Sequential(nn.BatchNorm1d(50), nn.Linear(50, 20)),
            nn.Sequential(nn.BatchNorm1d(20), nn.Linear(20, 2))
        ]

        self.layers = layers
        self.softmax = nn.Softmax(dim=1)

        for i in range(len(layers)):
            setattr(self, f"layer-{i}", layers[i])

    def forward(self, x):
        # x = self.max_pool_1(self.cnn1(x.view(x.size(), 1)))
        # x = self.max_pool_2(self.cnn2(x))

        for layer in self.layers[:-1]:
            x = F.relu(layer(x))

        x = self.layers[-1](x)

        if not self.training:
            x = self.softmax(x)

        return x


def make_sampler(loader):
    def sample_generator():
        while True:
            for x, y in loader:
                yield x, y

            yield None

    current_sample = None
    sampler = sample_generator()

    def next_sample():
        nonlocal current_sample
        current_sample = next(sampler)

    def current_sample():
        return current_sample

    return current_sample, next_sample


def train_one_cycle(model, trainset, loss_fn, optimizer):
    summed_loss = 0
    iters = 0

    model.train()

    for x, y in trainset:
        summed_loss += train_on_sample(model, x, y, loss_fn, optimizer)
        iters += 1

    return summed_loss / iters


def train_on_sample(model, x, y, loss_fn, optimizer):
    optimizer.zero_grad()

    y_hat = model(x)
    loss = loss_fn(y_hat, y)
    loss.backward()

    optimizer.step()

    return loss


def test(model, testset):
    corrects = 0
    N = 0

    model.eval()

    for x, y in testset:
        y_hat = model(x)

        # need a certainty of 0.6
        certainty_value = 0.8
        decision_vector = y_hat[:, 1] - torch.tensor([certainty_value] * y_hat.size()[0])
        predictions = decision_vector.gt(0)

        # predictions = torch.argmax(y_hat, dim=1)

        corrects += predictions.eq(y).sum().item()
        N += y.size()[0]

    return N, corrects


def predict(model, set):
    model.eval()
    predictions = []

    for x in set:
        y = torch.argmax(model(x), dim=1)

    return predictions

def test_on_sets(model, sets):
    for name, loader in sets:
        N, corrects = test(model, loader)
        acc = corrects / N
        print(f"current {name} accuracy: {acc}")


real_train_size = 700000
train_size = int(0.4 * real_train_size)
train_loader = data.DataLoader(get_data("train.csv", max_len=train_size), batch_size=train_size//40, shuffle=True)
print(f"finished loading {train_size} train lines")

validate_size = 50000
validate_loader = data.DataLoader(get_data("validate.csv", max_len=validate_size), batch_size=validate_size)
print(f"finished loading {validate_size} validate lines")

tester = functools.partial(test_on_sets, sets=[
    # ("train", train_loader),
    ("validate", validate_loader)])

model = Model()
# loss_fn = nn.CrossEntropyLoss()
loss_fn = nn.NLLLoss()
optimizer = torch.optim.Adam(model.parameters(), lr=0.07)

epochs = 100
torch.manual_seed(0)

for _ in range(epochs):
    loss = train_one_cycle(model, train_loader, loss_fn, optimizer)
    # print(loss.item())
    tester(model=model)

# repeats = 10
# current_sample, next_sample = make_sampler(train_loader)
# for _ in range(epochs):
#     import math
#
#     next_sample()
#     train_sample = current_sample()
#
#     while train_sample:
#         x, y = train_sample
#
#         min_loss, max_loss = math.inf, -math.inf
#         loss_sum = 0
#         latest = 0
#
#         for i in range(repeats):
#             loss = train_on_sample(model, x, y, loss_fn, optimizer).item()
#             loss_sum += loss
#             min_loss = min(min_loss, loss)
#             max_loss = max(max_loss, loss)
#             latest = loss
#
#         avg_loss = loss_sum / repeats
#
#         # print(f"min loss: {min_loss}, max loss: {max_loss}, average: {avg_loss}, latest: {latest}")
#
#         next_sample()
#         train_sample = current_sample()
