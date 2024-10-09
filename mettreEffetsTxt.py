import random
from effets_indesirables import recuperer_effets_indesirables

def modifier_url(specid):
    base_url = "https://base-donnees-publique.medicaments.gouv.fr/affichageDoc.php?specid={}&typedoc=N#Ann3bEffetsIndesirables"
    return base_url.format(specid)

def enregistrer_effets_dans_fichier(fichier_liste_medicaments, fichier_sortie):
    try:
        with open(fichier_liste_medicaments, 'r', encoding='utf-8') as f_medicaments, \
             open(fichier_sortie, 'w', encoding='utf-8') as f_sortie:

            medicaments = f_medicaments.readlines()
            
            medicaments_sample = random.sample(medicaments, min(100, len(medicaments)))

            for medicament in medicaments_sample:
                medicament = medicament.strip() 
                if not medicament: 
                    continue

                medicament_id = medicament.split()[0]
                url = modifier_url(medicament_id)

                effets = recuperer_effets_indesirables(url)

                f_sortie.write(f"Médicament: {medicament}\n")
                f_sortie.write(f"Effets secondaires : {effets}\n")
                f_sortie.write("-" * 40 + "\n")

        print(f"Les effets secondaires ont été enregistrés dans {fichier_sortie}")

    except Exception as e:
        print(f"Erreur lors de l'enregistrement : {e}")

def main():
    fichier_liste_medicaments = 'Medicaments.txt' 
    fichier_sortie_effets = 'EffetsSecondaires.txt'
    
    enregistrer_effets_dans_fichier(fichier_liste_medicaments, fichier_sortie_effets)

if __name__ == "__main__":
    main()
