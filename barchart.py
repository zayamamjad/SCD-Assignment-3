import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("anythingelse.csv")
df = df.sort_values(by="Popularity Count", ascending=False)
plt.figure(figsize=(10, 6))
plt.bar(df["Book Name"], df["Popularity Count"],color='aqua')
plt.xlabel("Book Name")
plt.ylabel("Popularity Count")
plt.title("Popularity Count of Books")
plt.xticks(rotation=45, ha="right")
plt.tight_layout()
plt.show()