import torch
import functools
import const
from timer import Timer


def train_on_sample(model, x, y, loss_fn, optimizer):
    optimizer.zero_grad()

    y_hat = model(x)
    loss = loss_fn(y_hat, y)
    loss.backward()

    optimizer.step()

    return loss


def train_one_cycle(model, train_set, loss_fn, optimizer, max_batches=0):
    summed_loss = 0
    iters = 0

    model.train()
    num_batches = len(train_set)

    total_work = Timer()

    t = Timer()
    t.start()

    total_load = 0
    load_timer = Timer()
    load_timer.start()

    total_work.start()

    for x, y in train_set:
        load_timer.stop()
        total_load += load_timer.time_passed()

        x, y = x.to(const.device), y.to(const.device)
        summed_loss += train_on_sample(model, x, y, loss_fn, optimizer)
        iters += 1

        if t.time_passed() >= 10:
            t.print(f"Time to finish {iters}/{num_batches} batches ")
            t.reset()
            t.start()

        if iters == max_batches:
            break

        load_timer.reset()
        load_timer.start()

    total_work.stop()

    print(f"Total time to load: {total_load}")
    total_work.print("Total train time")

    return summed_loss / iters


def float_formatter(number):
    return "{:.2f}".format(number)


def evaluate(model, test_set, predictor, file_out=None):
    corrects = 0
    N = 0

    model.eval()

    out_lines = []

    for x, y in test_set:
        x, y = x.to(const.device), y.to(const.device)

        y_hat = model(x)

        predictions = predictor(y_hat)

        corrects += predictions.eq(y).sum().item()
        N += y.size()[0]

        if file_out:
            for prediction, value in zip(y_hat, y):
                line = prediction.tolist() + [1.0 * value]

                out_lines.append(",".join(list(map(float_formatter, line))))

    if file_out:
        with open(file_out, "w") as f:
            for line in out_lines:
                f.write(line)
                f.write("\n")

    return N, corrects


evaluate_argmax = functools.partial(evaluate, predictor=lambda y: torch.argmax(y, dim=1))


def evaluate_certainty(certainty):
    return functools.partial(evaluate, predictor=lambda y: y[:, 1].gt(certainty).int())


def prediction_decider(y):
    return (y[:, 1] - y[:, 0]).gt(0.35).int()


def eval_sets(model, sets, file_out=None):
    accuracy = {}

    # eval = evaluate_certainty(0.5)
    eval = functools.partial(evaluate, predictor=prediction_decider)

    for name, loader in sets:
        N, corrects = eval(model=model, test_set=loader, file_out=file_out)
        acc = corrects / N

        accuracy[name] = acc

        print(f"current {name} accuracy: {acc}")

    return accuracy


def predict(model, set):
    model.eval()
    predictions = []

    for x, _ in set:
        x = x.to(const.device)
        y = model(x)
        # y = y[:, 1].gt(0.5).int()
        # y = (y[:, 1] - y[:, 0]).gt(0.5).int()
        y = prediction_decider(y)

        predictions += y.tolist()

    return predictions