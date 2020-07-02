import matplotlib.pyplot as plt
import sys
import numpy as np


def get_series(x, index):
    return [x[d] for d in range(index, 120, 4)]


xs = list(range(120 // 4))

i = 0

plot = [([], []), ([], [])]

pos = 0
neg = 0

for line in sys.stdin:
    i = i + 1

    # if i % 200:
    #     continue

    if not i % 1000:
        print(f"currently at: {i}")

    values = list(map(float, line.split(",")))
    zero = values[0]
    one = values[1]
    # sum = zero + one
    # if sum == 0:
    #     print("sup")
    #     sum = 0.1
    #
    # zero, one = zero / sum, one / sum

    cls = int(values[2])

    if one - zero >= 0.5:
        if cls == 0:
            neg += 1
        else:
            pos += 1

    if abs(zero) > 2 and abs(one) > 2:
        print("bad value")
        continue

    # if cls == 0:
    #     continue

    x, y = plot[cls]
    x.append(zero)
    y.append(one)

    # if cls == 1 and zero < one or \
    #     cls == 0 and zero > one:
    #     continue

    # color = "r" if cls == 1 else "b"
    # plt.scatter(zero, one, color=color)


print(f"pos: {pos}, neg: {neg}")
x, y = plot[0]
plt.scatter(x, y, color="b")

x, y = plot[1]
plt.scatter(x, y, color="r")

t = np.linspace(-2, 2, 2)
res = t + 1
plt.plot(t, res, linewidth=2)

# plt.plot(t, t, linewidth=2)

plt.xlim(-2, 2)
plt.ylim(-2, 2)
plt.show()
