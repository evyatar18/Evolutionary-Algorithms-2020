import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
import sys
from timer import Timer

if len(sys.argv) < 2:
    print("Must supply a filename", file=sys.stderr)
    exit(1)

file = sys.argv[1]
n = 120 + 4 * 2

if len(sys.argv) > 2:
    n = int(sys.argv[2])

features = list(map(str, range(n)))
used_features = features[:]
n_used = len(used_features)

# loading dataset into Pandas DataFrame
df = pd.read_csv(file, names=['target'] + features, usecols=used_features + ['target'])

X = df.loc[:, used_features].values
Y = df.loc[:, ['target']].values

X = StandardScaler().fit_transform(X)

pca = PCA(n_components=n_used)

X = pca.fit_transform(X)
print(list(map(lambda x: "{:.2f}%".format(x * 100), pca.explained_variance_ratio_)), file=sys.stderr)

t, last_update = Timer(), Timer()
t.start()
last_update.start()

i = 0
for x, y in zip(X, Y):
    i += 1

    try:
        y = int(y)
    except:
        y = 1

    arr = [str(y)] + list(map(lambda val: "{:.5f}".format(val), list(x)))
    print(",".join(arr))

    if last_update.time_passed() > 10:
        print(f"Time to load {i} lines {t}", file=sys.stderr)
        last_update.restart()

print(f"Time to load {i} lines {t}", file=sys.stderr)
