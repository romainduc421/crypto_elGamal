# crypto_elGamal
### Description
* implementation de l'algorithme de chiffrement d'El Gamal dans le cadre de l'UE optionnelle d'Introduction à la cryptographie en M1 info

### Utilisation
~~~
git clone https://github.com/romainduc421/crypto_elGamal
cd crypto_elGamal/src/
javac Main.java; java -ea Main [-all]
~~~
* ```-all``` permet de tester les assertions de toutes les itérations suivantes (exécution plus lente : une trentaine de secondes)
* Ce paramètre est optionnel et par défaut seules les 5 premières itérations seront testées
* ```-ea``` permet d'activer les assertions
* Le fichier de sortie ```test.txt``` se trouvera sous le répertoire src

### Objectifs
1. Comprendre le fonctionnement du système de chiffrement El Gamal (chiffrement à clé publique ou asymétrique)
2. Implémenter le chiffrement d'El Gamal avec des paramètres imposés
3. Tester votre implémentation
4. Tester une propriété particulière d'El Gamal (homomorphisme)