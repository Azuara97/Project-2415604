package luis.azuara.necuiltonolli.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val Id: Int,
    val Name: String,
    val Type: String,
    val Descrip: String,
    val Status: String,
    val imgURL: String
) : Parcelable

fun filterProducts(
    list: List<Product>,
    name: String? = null,
    type: String? = null
): List<Product> = list.filter {
    product -> (name == null ||
                product.Name.lowercase().contains(name.lowercase()) ||
                product.Type.lowercase() == type?.lowercase())
}