pip install requests beautifulsoup4 - Pour la lecture de la page WEB des notices de médicaments. 
Lien vers la page WEB : https://base-donnees-publique.medicaments.gouv.fr/affichageDoc.php?specid=60234100&typedoc=N#Ann3bEffetsIndesirables

pip install transformers - Pour le modèle de NLP qui permet de poser des questions et d'extraire les informations des effets secondaires des médicaments.


Pour l'exécution de IA_arguement, voici un exemple de commande à donner :

#Choix 1 qui permet de donner la liste des médicaments contenant "paracetamol"
python .\IA_argument.py 1 "paracetamol"

#Choix 2 qui permet d'avoir les médicaments qui ont des effets secondaires sur le thorax
python .\IA_argument.py 2 "thorax"

#choix 3 qui permet de faire une phrase type que l'on peut copier pour la mettre dans une IA (à défaut d'avoir l'API ChatGPT...)
python .\IA_argument.py 3 "thorax"

#Choix 4 qui permet à partir de plusieurs médicaments, dire si le symptôme rougeur fait partie des effets secondaires de ces médicaments et si oui, à quelle fréquence
Python .\IA_argument.py 4 "paracetamol, vitamine" --symptome "rougeur"

#Choix 5 qui permet de donner notre choix de médicament parmi la liste donnée par le choix 1 afin d'avoir ses effets indésirables
Python .\IA_argument.py 5 "paracetamol" --choix_medicament 2

