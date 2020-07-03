import torch
import torch.utils.data as data
import torch.nn as nn

import functools

import random
from model import Model
from data_getter import get_data
from model_utils import eval_sets, train_one_cycle, predict
from const import device

# def make_sampler(loader):
#     def sample_generator():
#         while True:
#             for x, y in loader:
#                 yield x, y
#
#             yield None
#
#     current_sample = None
#     sampler = sample_generator()
#
#     def next_sample():
#         nonlocal current_sample
#         current_sample = next(sampler)
#
#     def current_sample():
#         return current_sample
#
#     return current_sample, next_sample


def write_predictions(filename, predictions):
    with open(filename, "w") as f:
        for prediction in predictions:
            f.write(str(prediction))
            f.write("\n")


def do_training(model, save_path):
    real_train_size = 700000
    train_size = int(0.2 * real_train_size)
    train_loader = data.DataLoader(get_data("train.csv", max_len=train_size), batch_size=min(train_size // 200, 2000),
                                   shuffle=False, pin_memory=True, num_workers=20)
    print(f"finished loading {train_size} train lines")

    validate_size = 50000
    validate_loader = data.DataLoader(get_data("validate.csv", max_len=validate_size), batch_size=validate_size,
                                      pin_memory=True)
    print(f"finished loading {validate_size} validate lines")

    tester = functools.partial(eval_sets, sets=[
        ("train", train_loader),
        ("validate", validate_loader)])

    loss_fn = nn.CrossEntropyLoss()
    # loss_fn = nn.NLLLoss()
    optimizer = torch.optim.Adam(model.parameters(), lr=0.0005)

    # 94668407
    seed = random.randint(0, 100000000)
    epochs = 1000
    torch.manual_seed(seed)
    print(f"seed: {seed}")

    threshold = 0.62

    for it in range(epochs):
        loss = train_one_cycle(model, train_loader, loss_fn, optimizer)
        print(loss.item())

        results = tester(model=model)

        print("========================")
        print(f"done with iteration {it}")
        if results["validate"] >= threshold:
            if input("stop for now? (y/n)").startswith("y"):
                break
            else:
                ts = input("new threshold(leave blank if no change wanted): ")

                if len(ts) > 0:
                    try:
                        threshold = float(ts)
                    except:
                        pass

    model.save(save_path)


def do_tests(model, out_file):
    print(f"saving test results of given model to {out_file}")
    test_size = 50000
    test_loader = data.DataLoader(get_data("test.csv", max_len=test_size),
                                  batch_size=test_size, pin_memory=True)
    print(f"finished loading {test_size} test lines")

    write_predictions(out_file, predict(model, test_loader))


def do_validation(model):
    validate_size = 50000
    validate_loader = data.DataLoader(get_data("validate.csv", max_len=validate_size),
                                      batch_size=validate_size,
                                      pin_memory=True)
    print(f"finished loading {validate_size} validate lines")

    tester = functools.partial(eval_sets, sets=[
        ("validate", validate_loader)])

    tester(model=model, file_out="validation_out.txt")


save_path = "my_model_4"
test_out = "test-results.txt"

model = Model().to(device)
do_training(model, save_path)
do_tests(model, test_out)

# model = Model.Load(save_path)
# model = Model2()
# model.load_state_dict(torch.load("my-model"))
# model.eval()
# print("finished loading model")
# do_validation(model)
# do_tests(model, test_out)


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
