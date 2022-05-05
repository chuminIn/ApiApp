package jp.techacademy.bunta.tsurumi.apiapp

interface FragmentCallback {
    // Itemを押したときの処理
    //fun onClickItem(url: String)
    // お気に入り追加時の処理
    fun onAddFavorite(shop: Shop)
    // お気に入り削除時の処理
    fun onDeleteFavorite(id: String)
    fun onClickItem(favoriteShop: FavoriteShop) // クーポン詳細ページでもお気に入りの追加削除
    fun onClickItem(shop: Shop) // クーポン詳細ページでもお気に入りの追加削除
}