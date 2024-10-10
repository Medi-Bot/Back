import requests
from bs4 import BeautifulSoup
import re

def recuperer_effets_indesirables(url):
    response = requests.get(url)

    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')

        # Le texte brut de la page
        texte = soup.get_text()

        # Mot clé qui indique le début de la section des effets indésirables
        debut_section = "QUELS SONT LES EFFETS INDESIRABLES EVENTUELS ?"
        fin_section = "5. COMMENT CONSERVER"  # Mot clé pour indiquer la fin de la section

        # Chercher le début de la section des effets indésirables
        if debut_section in texte:
            texte_section = texte.split(debut_section, 1)[1]
        else:
            print("Section des effets indésirables non trouvée.")
            return {}

        # Chercher la fin de la section en arrêtant à la phrase clé
        if fin_section in texte_section:
            texte_section = texte_section.split(fin_section, 1)[0]

        # Supprimer la section de déclaration des effets secondaires
        if "Déclaration des effets secondaires" in texte_section:
            texte_section = texte_section.split("Déclaration des effets secondaires", 1)[0]

        # Mots clés pour les fréquences
        mots_clefs = [
            "très fréquents", "fréquents", "peu fréquents", "rares", 
            "fréquence indéterminée", "indéterminée"
        ]

        resultats = {mot: set() for mot in mots_clefs}  # Utiliser un set pour éviter les doublons
        effets_rencontres = set()  # Pour suivre les doublons potentiels

        # Pour chaque mot clé, on recherche les effets indésirables correspondants
        for mot in mots_clefs:
            # Utilisation d'une expression régulière pour extraire le texte
            regex_pattern = rf"{re.escape(mot)}.*?(?=\n(?:{'|'.join(map(re.escape, mots_clefs))})|\n{fin_section}|\Z)"
            effets_trouves = re.findall(regex_pattern, texte_section, re.DOTALL | re.IGNORECASE)

            

            # Filtrer les effets pour ne garder que les textes pertinents
            if effets_trouves:
                for effet in effets_trouves:
                    effet_nettoye = effet.strip().replace('·', '').strip()  # Supprimer les puces inutiles

                    # Vérification de doublon
                    if effet_nettoye in effets_rencontres:
                        print(f"Doublon trouvé pour '{effet_nettoye}' dans la catégorie '{mot}'.")
                    else:
                        effets_rencontres.add(effet_nettoye)
                        resultats[mot].add(effet_nettoye)  # Ajouter l'effet si pas encore rencontré

        # Format de sortie
        print("\nEffets indésirables et leur fréquence :")
        for cle, effets in resultats.items():
            if effets:  # Afficher seulement si des effets existent
                print(f"\n{cle.capitalize()} :")
                for effet in effets:
                    print(f" - {effet}")  # Afficher les effets

        # Convertir les sets en listes avant de retourner
        return {k: list(v) for k, v in resultats.items()}  # Retourner sous forme de liste pour cohérence
    else:
        print(f"Erreur lors de l'accès à la page : {response.status_code}")
        return {}
