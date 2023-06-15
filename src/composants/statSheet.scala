package composants

class statSheet(var regen: Int = 1,
                var maxHealth: Int = 1,
                var bulletSpeed: Int = 1,
                var bulletDamage: Int = 1,
                var reload: Int = 1,
                var movementSpeed: Int = 1) {}
//les statistiques de level up sont cappées a 8, le niveau de base d'un joueur dans toutes les stats est 1