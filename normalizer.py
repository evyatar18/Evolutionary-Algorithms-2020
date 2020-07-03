from __future__ import print_function
import numpy
import sys
from timer import Timer

max_lines = -1

if len(sys.argv) > 1:
    max_lines = int(sys.argv[1])


def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)


def normalize(repeating_features, cycle_size):
    from functools import reduce

    out_features = []
    out_params = []

    for i in range(cycle_size):
        series = repeating_features[i::cycle_size]
        mean, std = numpy.mean(series), numpy.std(series)
        if std < 1e-8:
            std = 1e-8

        out_features.append([(x - mean) / std for x in series])
        out_params.append((mean, std))

    out_series = []

    for x in zip(*out_features):
        out_series += list(x)

    out_series += reduce(lambda x, y: x + list(y), out_params, [])
    return out_series


i = 0
t, last_update = Timer(), Timer()
t.start()
last_update.start()

for line in sys.stdin:
    if max_lines == i:
        break
    i += 1

    line = line.split(",")
    cls, features = line[0], list(map(float, line[1:]))

    f_out = list(map(lambda x: "{:.5f}".format(x), normalize(features, 4)))

    print(",".join([cls] + f_out))

    if last_update.time_passed() > 10:
        eprint(f"Time to load {i} lines: {t} passed.")
        last_update.restart()

eprint(f"Time to load {i} lines: {t} passed.")