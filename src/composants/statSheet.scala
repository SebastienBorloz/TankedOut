package composants

class statSheet(var regen: Int = 0,
                var maxHealth: Int = 0,
                var bodyDamage: Int = 0,
                var bulletSpeed: Int = 0,
                var bulletPenetration: Int = 0,
                var bulletDamage: Int = 0,
                var reload: Int = 0,
                var movementSpeed: Int = 0) {}
//les statistiques de level up sont cappées a 8, le niveau de base d'un joueur dans toutes les stats est 1