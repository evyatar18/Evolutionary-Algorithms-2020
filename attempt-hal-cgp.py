import torch
import torch.utils.data as data

import functools
import matplotlib.pyplot as plt
import numpy as np
import scipy.constants

import cgp
from typing import Tuple, List

class Timer:
    from time import time as current_time

    def __init__(self):
        self.started = False
        self.start_time = 0
        self.stop_time = 0

    def reset(self):
        self.__init__()

    def start(self):
        self.start_time = self.current_time()
        self.started = True

    def stop(self):
        self.stop_time = self.current_time()
        self.started = False

    def get_value(self):
        if self.started:
            return self.current_time() - self.start_time

        else:
            return self.stop_time - self.start_time

    def print(self, start_msg=""):
        print(start_msg, self.get_value(), "seconds passed.")


num_total_inputs = 120
num_per_cycle = 4
cycles = 30
num_used = cycles * num_per_cycle
num_skip = num_total_inputs - num_used


def initialize_set(set_size: int, filepath: str) -> List[Tuple]:
    s = []

    with open(filepath, 'r') as f:
        for _ in range(set_size):
            # read the line and split by ','
            line = f.readline().split(',')

            # get output
            # out = torch.tensor([0, 1] if line[0] == '1' else [1, 0], )
            out = int("1" if line[0] == "?" else line[0])
            out = torch.FloatTensor([0, 1] if out == 1 else [1, 0])

            # current_output = (1.0, 0.0) if float(line[0]) == 0.0 else (0.0, 1.0)
            # assert argmax(current_output) == int(line[0])
            # output_set.append(current_output)

            # convert the rest of the line (the features) to float
            input = [float(element) for element in line[1 + num_skip:]]

            # normalize them
            input = [(input[i] - 3698.496951809524) / 15040.936788723327 if i % 2 == 0 else
                     (input[i] - 99.99853944637789) / 0.15993966023833134 for i in range(num_used)]

            s.append((torch.FloatTensor(input), out))

    return s


training_set_size = 10000
validation_set_size = 50000
test_set_size = 50000
num_gens = 1000

timer = Timer()
timer.start()

train_ds = initialize_set(training_set_size, "train.csv")
validate_ds = initialize_set(validation_set_size, "validate.csv")
test_ds = initialize_set(training_set_size, "test.csv")

timer.stop()
timer.print("finished loading")

train_loader = data.DataLoader(
    train_ds, batch_size=1000, shuffle=True,
    num_workers=20, pin_memory=True, sampler=None)
validation_loader = data.DataLoader(
    validate_ds, batch_size=1000, shuffle=True,
    num_workers=20, pin_memory=True, sampler=None)
test_loader = data.DataLoader(
    test_ds, batch_size=1000, shuffle=True,
    num_workers=20, pin_memory=True, sampler=None)

use_cuda = torch.cuda.is_available()
device = torch.device("cuda:0" if use_cuda else "cpu")


def inner_objective(f, current_set):
    x, y = current_set()
    print(x.dtype, y.dtype)
    # print(type(x))
    # print(type(x.float()))
    print(x)
    print(f)
    y_tag = f(x)

    # print(x.size())
    # print(y_tag.size(), y.size())

    return torch.nn.MSELoss()(y_tag, y)


def create_objective(loader: data.DataLoader):
    current_set = None

    def set_generator():
        while True:
            for input, target in loader:
                # input, target = input.to(device), target.to(device)
                input, target = input.float(), target.float()
                yield input, target

    gen = set_generator()

    def next_set():
        nonlocal current_set

        try:
            current_set = next(gen)
        except StopIteration:
            raise Exception("fuck this world")

    def get_set():
        return current_set

    def objective_function(individual):
        if individual.fitness:
            print("used saved fitness")
            return individual

        # torch.manual_seed = seed
        # f = individual.to_torch().to(device)
        f = individual.to_torch()

        loss = inner_objective(f, get_set)
        individual.fitness = -loss.float().item()

        next_set()

        return individual

    next_set()

    return objective_function, next_set, get_set


def evolution():
    population_params = {"n_parents": 5, "mutation_rate": 0.05, "seed": 818821}

    genome_params = {
        "n_inputs": num_used,
        "n_outputs": 2,
        "n_columns": 20,
        "n_rows": 5,
        "levels_back": None,
        "primitives": (cgp.Add, cgp.Sub, cgp.Mul, cgp.Parameter, cgp.ConstantFloat, cgp.Div),
    }

    ea_params = {"n_offsprings": 4, "n_breeding": 4, "tournament_size": 1, "n_processes": 1}

    evolve_params = {"max_generations": 2000, "min_fitness": 0.0}

    # use an uneven number of gradient steps so they can not easily
    # average out for clipped values
    local_search_params = {"lr": 1e-3, "gradient_steps": 9}

    obj, next_set, current_set = create_objective(train_loader)

    pop = cgp.Population(**population_params, genome_params=genome_params)

    # define the function for local search; parameters such as the
    # learning rate and number of gradient steps are fixed via the use
    # of `partial`; the local_search function should only receive a
    # population of individuals as input
    # local_search = functools.partial(
    #     cgp.local_search.gradient_based,
    #     objective=functools.partial(inner_objective, current_set=current_set),
    #     **local_search_params,
    # )

    def local_search(ind):
        print("local search")
        return ind

    ea = cgp.ea.MuPlusLambda(**ea_params, local_search=local_search)

    history = {}
    history["champion"] = []
    history["fitness_parents"] = []

    def recording_callback(pop):
        history["champion"].append(pop.champion)
        history["fitness_parents"].append(pop.fitness_parents())

    i = 0

    def general_callback(pop):
        try:
            recording_callback(pop)
        except:
            pass

        nonlocal i

        i += 1
        print(f"current iteration: {i}")

        # if not i % 10:
        #     next_set()

    cgp.evolve(
        pop, obj, ea, **evolve_params, print_progress=True, callback=general_callback,
    )

    return history, pop.champion


if __name__ == "__main__":
    width = 9.0

    fig = plt.figure(figsize=(width, width / scipy.constants.golden))

    ax_fitness = fig.add_subplot(121)
    ax_fitness.set_xlabel("Generation")
    ax_fitness.set_ylabel("Fitness")
    ax_fitness.set_yscale("symlog")

    ax_function = fig.add_subplot(122)
    ax_function.set_ylabel(r"$f(x)$")
    ax_function.set_xlabel(r"$x$")

    history, champion = evolution()

    print(f"Final expression {champion.to_sympy()[0]} with fitness {champion.fitness}")

    history_fitness = np.array(history["fitness_parents"])
    ax_fitness.plot(np.max(history_fitness, axis=1), label="Champion")
    ax_fitness.plot(np.mean(history_fitness, axis=1), label="Population mean")

    plt.savefig("example_differential_evo_regression.pdf", dpi=300)
