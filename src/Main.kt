import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.util.*
import kotlin.random.Random
interface RepairService{
 fun repairPhone():Boolean
}

abstract class PhoneStore(val city: String){
 val phoneStock = mutableMapOf<String,Pair<Int,Double>>()
 val salesStats = mutableMapOf<String,Int>()

 abstract fun greetCustomer()
 open fun sellPhone(model: String){
  val stock = phoneStock[model]?.first ?: 0
  if (stock > 0){

  phoneStock[model] = stock - 1 to phoneStock[model]!!.second
  salesStats[model] = salesStats.getOrDefault(model, 0) + 1
   println("Телефон $model куплен !!!")
  }else{
   println("Телефон $model отсутствует на складе.")
  }
 }

 open fun showStatistics(){
  println("Статистика покупок в магазине $city")
  var totalSales = 0.0
  salesStats.forEach{(model, count) ->
   val price = phoneStock[model]?.second ?: 0.0
   println("Модель $model: Продано $count шт. Цена за единицу: $price")
   totalSales += count * price
  }
  println("Оющая сумма продаж: $totalSales")
 }
}
class CityStore(city: String): PhoneStore(city), RepairService{
 private var repairOffered = false
 override fun greetCustomer() {
  println("Добро пожаловать в магазин телефонов в городе $city !!!")
 }

 override fun repairPhone(): Boolean {
  return if (!repairOffered) {
   println("Хотите отремонтировать телефон? (Да/Нет)")
   val resonse = readln()!!
   repairOffered = true
   if (resonse.lowercase(Locale.getDefault()) == "Да"){
    println("Телефон успешно отремонтирован")
    true
   }else{
    println("Ремонт отклонен")
    false
   }
  }else{
   println("Ремонт телефона уже предлагался.")
   false
  }
 }
}


fun main(){
val store1 = CityStore("Москва")
val store2 = CityStore("Санкт-Петербург")

store1.phoneStock["iPhone 15"] = 5 to 140000.0
store1.phoneStock["Xiaomi 14"] = 4 to 70000.0
 store2.phoneStock["iPhone 15"] = 2 to 135000.0
 store2.phoneStock["Xiaomi 14"] = 1 to 55000.0

 while (true){
  println("Добро пожаловать в интернет магазин телефонов")
  println("Выберите город: 1 - Москва, 2 - Санкт-Петербург, 0 - Выйти")
  when (readln()!!.toInt()){
   1 -> interactWithStore(store1)
   2 -> interactWithStore(store2)
   0 -> {
    println("До свидания!")
    return
   }
   else -> println("Неверный выбор, попробуйсе снова")

  }
 }
}
fun interactWithStore(store: PhoneStore){
 store.greetCustomer()
 while (true){
  println("Выберите действие: ")
  println("1 - Купить телефон")
  println("2 - Показать статистику")
  println("3 - Ремонт телефона (только для определенныйх магазинов)")
  println("0 - Вернуться назад")

  when (readln()!!.toInt()){
   1 ->{
    println("Доступные телефоны: ${store.phoneStock.keys}")
    println("Введите модель телефона для покупки: ")
    val model = readln()!!
    if (model in store.phoneStock.keys){
     store.sellPhone(model)
    }else{
     println("Модель не найдена")
    }
   }
   2 -> store.showStatistics()
   3 -> if (store is RepairService) store.repairPhone()
   0 -> return
   else -> println("Неверный выбор, попробуйте снова")
  }

 }
}
