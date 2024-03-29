# Le jeu "Le Jeu" de Loïc Herman et Kelvin Kappeler

## Note importante

> **ATTENTION**: Fichiers manquants dans `res/`

Afin de soumettre notre projet, nous avons dû omettre certains fichiers que nous avons rajoutés dans `res/`, ils sont disponibles à l'addresse suivante:
https://drive.google.com/drive/folders/1DLTQZ81njx7j2XYH97izlQByb7ZLf6w6?usp=sharing

Il suffira de glisser le dossier `res/` de `0_CUSTOM-RESOURCES` dans le dossier racine de notre projet, et les sprites et sons personnalisés (et leur sources décrites dans `README.md`) seront ajoutés automatiquement.

Le drive contient aussi l'image de l'hierarchie utilisée dans notre projet. (Comme décrit dans `CONCEPTION.md`) 

## Description

Vous vous réveillez dans une maison, où une personne vous annonce que le roi a été emprisonné par un monstre des ténèbres.

C'est à vous d'utiliser vos trois bombes trouvées sous la télévision pour acheter l'équipement nécessaire au village.

Il y a eu une rumeur dans le village qu'une grotte dont un éboulement a condamné l'entrée qui contiendrait un trésor pouvant vous aider à compléter votre quête.

### Contrôles
- UP - Move Up
- RIGHT - Move Right
- DOWN - Move Down 
- LEFT - Move Left 
- TAB - Cycle Inventory Item
- SPACE - Use Item 
- ENTER - Accept Game Over and Buy Items 
- E - Interact (view interaction) 
- I - Open and Close Inventory and Close Shop

Si le mode de test est activé:
- B - Spawn Bombs 
- S - Spawn Flame Skull 
- L - Spawn Log Monster

## Mode de test
Nous avons implémenté un mode de test qui permet d'utiliser les contrôles de triche mentionnés ci-dessus et donnent au joueur tous les objets
requis pour finir, c'est-à-dire que le joueur va recevoir un arc, des flèches, la clé du château et tous les autres objets du jeu dès l'instantiation.

Pour l'activer, il est nécessaire d'aller dans l'interface `Test` du fichier `Test.java` dans le packetage `ch.epfl.cs107.play.game.arpg` et de remplacer le boolean `MODE` à `true`.

## Solution

#### But du jeu
Vaincre le Dark Lord et sauver le roi.

#### Comment vaincre le Dark Lord
Le seigneur des ténèbres situé devant le chateau tout en haut est insensible à tout dégats physiques, il n'est possible de le blesser qu'au moyen de magie. Afin de produire cette magie, il faut vous procurer un baton magique.

Le Dark Lord possède 3 pouvoirs:
- l'invocation de crânes volants
- l'apparition de flammes
- une téléportation si le joueur s'approche trop

#### Comment obtenir le baton magique
Le baton magique se trouve dans un temple caché protégé par des monstres troncs.

Pour y accéder, il faut tirer avec un arc, en ayant au moins une flèche dans l'inventaire, sur le levier situé en bas à droite de la zone Route.

#### Comment tuer les monstres troncs et faucher l'herbe
Afin de casser l'herbe et tuer les monstres troncs, le joueur doit trouver l'épée qui est cachée en haut de la colline du village, accessible depuis la Ferme.
Une interaction de distance permettra au joueur de récupérer l'épée et tuer les monstres troncs pour récupérer quelques pièces.

#### Comment obtenir l'arc et la flèche
Un vendeur situé à l'échoppe du village possède des flèches, bombes et un arc qu'il peut revendre contre un peu d'argent.
Ses tarifs sont décrits devant l'échoppe, mais le joueur n'a pas assez d'argent de base pour tout acheter.

#### Comment obtenir de l'argent
Comme mentionné précédemment le joueur peut obtenir des pièces en tuant les monstres troncs ou en fauchant de l'herbe.
Mais, au bas de la colline du Nord du village se trouve une entrée vers une grotte dans laquelle a eu lieu un éboulement.

Le joueur, avec les trois bombes qu'il reçoit au départ peut en faire exploser une devant l'entrée qui va dégager les pierres et le permettre d'entrer dans la grotte.
Cette grotte est envahie de monstres troncs, mais possède un paquet de pièces au fond, ainsi que quelques coeurs à côté de l'entrée. Attention! la grotte n'est pas très lumineuse.

#### Fin du jeu 
Une fois l'arc et les flèches obtenus, le joueur peut prendre le baton magique dans le temple et vaincre le Dark Lord. Celui-là laissera derrière lui après sa mort une clé qui permettra d'ouvrir la porte du château.

# Sources des sprites et sons
## Sons

Les fichiers de sons commençant par `sw.` proviennent du jeu Stardew Valley. 
Il est important de préciser que nous ne prévoyons pas de diffuser le jeu et avons utilisé ces bruitages dans un but personnel uniquement.
Si le code venait à être diffusé, ces bruitages seront supprimés et remplacés par des bruitages dont la license est plus claire que "utilisation personelle uniquement".

Les autres fichiers de sons sont des enregistrements de la campagne sécheronne efféctués par Loïc Herman. 

## Sprites

Tous les sprites ajoutés utilisent des assets venant de Opengameart, il est fort probable que ce soit le même que celui utilisé
pour concevoir les backgrounds de la maquette car beaucoup d'entre eux sont pareils.
Certains assets ont été modifiés comme celui du rocher de l'épée.

