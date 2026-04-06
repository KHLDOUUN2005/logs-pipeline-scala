import matplotlib.pyplot as plt
import pandas as pd

# =====================
# 1. Top pages visitées
# =====================
df_pages = pd.read_csv("output/top_pages.csv")
plt.figure(figsize=(10, 5))
plt.barh(df_pages["url"], df_pages["count"], color="#4C72B0")
plt.title("Top pages les plus consultées")
plt.xlabel("Nombre de requêtes")
plt.tight_layout()
plt.savefig("output/top_pages.png")
plt.close()

# =====================
# 2. Répartition codes HTTP
# =====================
df_codes = pd.read_csv("output/codes_http.csv")
plt.figure(figsize=(7, 7))
plt.pie(df_codes["count"], labels=df_codes["status"].astype(str),
        autopct="%1.1f%%", colors=["#2ecc71","#e74c3c","#f39c12","#9b59b6","#3498db","#1abc9c"])
plt.title("Répartition des codes HTTP")
plt.tight_layout()
plt.savefig("output/codes_http.png")
plt.close()

# =====================
# 3. Trafic par pays
# =====================
df_pays = pd.read_csv("output/trafic_par_pays.csv")
plt.figure(figsize=(10, 5))
plt.bar(df_pays["country"], df_pays["nb_requetes"], color="#E76F51")
plt.title("Trafic par pays")
plt.xlabel("Pays")
plt.ylabel("Nombre de requêtes")
plt.tight_layout()
plt.savefig("output/trafic_par_pays.png")
plt.close()

# =====================
# 4. Trafic par heure
# =====================
df_heure = pd.read_csv("output/trafic_par_heure.csv")
plt.figure(figsize=(12, 5))
plt.plot(df_heure["hour"], df_heure["count"], marker="o", color="#2A9D8F", linewidth=2)
plt.title("Trafic par heure de la journée")
plt.xlabel("Heure")
plt.ylabel("Nombre de requêtes")
plt.xticks(range(0, 24))
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.savefig("output/trafic_par_heure.png")
plt.close()

# =====================
# 5. Top IPs actives
# =====================
df_ips = pd.read_csv("output/top_ips.csv")
plt.figure(figsize=(10, 5))
plt.bar(df_ips["ip"], df_ips["nb_requetes"], color="#457B9D", label="Requêtes totales")
plt.bar(df_ips["ip"], df_ips["nb_erreurs"], color="#E63946", label="Erreurs")
plt.title("Top IPs les plus actives")
plt.xlabel("Adresse IP")
plt.ylabel("Nombre de requêtes")
plt.legend()
plt.tight_layout()
plt.savefig("output/top_ips.png")
plt.close()

print("✅ 5 graphiques générés dans output/")