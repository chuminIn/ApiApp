package jp.techacademy.bunta.tsurumi.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity: AppCompatActivity()  {

    // 取得したJsonデータを解析し、Shop型オブジェクトとして生成したものを格納するリスト
    private val items = mutableListOf<Shop>()
    // 一覧画面から登録するときのコールバック（FavoriteFragmentへ通知するメソッド)
    var onClickAddFavorite: ((Shop) -> Unit)? = null
    // 一覧画面から削除するときのコールバック（ApiFragmentへ通知するメソッド)
    var onClickDeleteFavorite: ((Shop) -> Unit)? = null
    // Itemを押したときのメソッド
    var onClickItem: ((String) -> Unit)? = null
    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }
    var favoriteShop = FavoriteShop()
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        favoriteShop = intent.getSerializableExtra(KEY_FAVORITE_SHOP) as? FavoriteShop ?: return { finish() }()
        webView.loadUrl(favoriteShop.url)
        isFavorite = FavoriteShop.findBy(favoriteShop.id) != null

        favoriteImageView2.setOnClickListener {
            if (isFavorite){
                FavoriteShop.insert(favoriteShop)
            }else{
                FavoriteShop.delete(favoriteShop.id)
            }
        }
    }

    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_FAVORITE_SHOP = "key_favorite_shop"
        private const val VIEW_PAGER_POSITION_API = 0
        private const val VIEW_PAGER_POSITION_FAVORITE = 1
        fun start(activity: Activity, url: String) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, url))
        }
        fun start(activity: Activity, favoriteShop: FavoriteShop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_FAVORITE_SHOP, favoriteShop))
        }
    }




    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }

}

