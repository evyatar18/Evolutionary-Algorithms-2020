import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import PCA
import matplotlib.pyplot as plt

file_path = "train.csv"

features = list(map(str, range(120)))
cls = ["class"]
order = cls + features

df = pd.read_csv(file_path, names=order, nrows=15000)

x = df.loc[:, features].values
y = df.loc[:, cls].values

x = StandardScaler().fit_transform(x)

pca = PCA(n_components=2)

pcs = pca.fit_transform(x)

principalDf = pd.DataFrame(data=pcs, columns=['principal component 1', 'principal component 2'])

finalDf = pd.concat([principalDf, df[cls]])


fig = plt.figure(figsize = (8,8))
ax = fig.add_subplot(1,1,1)
ax.set_xlabel('Principal Component 1', fontsize = 15)
ax.set_ylabel('Principal Component 2', fontsize = 15)
ax.set_title('2 component PCA', fontsize = 20)
targets = ['1', '0']

for target in zip(targets):
    indicesToKeep = finalDf[cls] == target
    print(indicesToKeep)
    ax.scatter(finalDf.loc[indicesToKeep, 'principal component 1']
               , finalDf.loc[indicesToKeep, 'principal component 2']
               , s = 50)
ax.legend(targets)
ax.grid()
