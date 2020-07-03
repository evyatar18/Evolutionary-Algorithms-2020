import torch
import numpy
from const import num_params, length_params, line_length, line_inputs


class DataGetter:
    def __init__(self, lines):
        self.lines = lines
        self.parsed = {}
        self.num_parsed = 0

    def __getitem__(self, index):
        if index in self.parsed:
            return self.parsed[index]

        if self.num_parsed > 1.5 * len(self):
            print(f"parsed: {self.num_parsed}")

        self.parsed[index] = parse_line(self.lines[index])
        self.num_parsed += 1
        return self.parsed[index]

    def __len__(self):
        return len(self.lines)


class DataParser(torch.utils.data.Dataset):
    def __init__(self, lines):
        self.parsed = []
        self.parse_lines(lines)
        self.len = len(lines)

    def parse_lines(self, lines):
        from timer import Timer

        t = Timer()
        total = Timer()

        total.start()
        t.start()

        num_lines = len(lines)
        num_loaded = 0

        for line in lines:
            self.parsed.append(parse_line(line))

            num_loaded += 1

            if t.time_passed() >= 10:
                t.print(f"Time taken to load {num_loaded}/{num_lines}:")
                t.reset()
                t.start()

        t.stop()
        total.print("Time taken to load all lines")

    def __len__(self):
        return self.len

    def __getitem__(self, item):
        return self.parsed[item]


def get_data(file_name, max_len=0):
    ret = []
    i = 0
    with open(file_name, "r") as f:
        for line in f:
            i += 1

            ret.append(line)

            if i == max_len:
                break

    return DataParser(ret)


def parse_line(line):
    # line = line.replace(" ", "")
    params = line.split(",")
    assert len(params) == line_length, "wrong param number when reading data! Is currently " + str(len(params))

    data = list(map(float, params[1 + (line_inputs - num_params):]))
    # data = (data - data.min()) / (data.max() - data.min())

    # avg_0 = float(numpy.average(data[::2]))
    # std_0 = float(numpy.std(data[::2]))
    #
    # data = [data[i] - 100 if i % 2 else (data[i] - avg_0) / std_0 for i in range(len(data))]
    # data = list(map(float, data))
    # data.append(avg_0)
    # data.append(std_0)
    # data += [float(0.0)] * 2

    label = 0

    try:
        label = int(params[0])
    except:
        pass

    return torch.tensor(data).view(1, -1), label
