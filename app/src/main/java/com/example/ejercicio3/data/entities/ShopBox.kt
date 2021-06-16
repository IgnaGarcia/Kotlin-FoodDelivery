package com.example.ejercicio3.data.entities

data class ShopBox(
        val plateList : MutableList<Plate> = mutableListOf(),
        val countMap : MutableMap<Int, Int> = mutableMapOf(),
        var total : Double = 0.0,
        var onCountChange : OnCountChange? = null
){
        fun addPlate(plate : Plate){
                plateList.add(plate)
                countMap[plate.id] = 1
                onCountChange?.onCountChange()
        }

        fun removePlate(plate : Plate){
                plateList.remove(plate)
                countMap.remove(plate.id)
                onCountChange?.onCountChange()
        }

        fun calcTotal() : Double{
                total = 0.0
                plateList.forEach {
                        total += it.pricePerServing * countMap[it.id]!!
                }
                return total
        }

        fun clear(){
                plateList.clear()
                countMap.clear()
                total = 0.0
        }

        fun changeCount(plate : Plate, opt : Int){
                if (!countMap.containsKey(plate.id)) addPlate(plate)
                if(opt == 1) countMap[plate.id] = countMap[plate.id]!! + 1
                else if (opt == 0){
                        countMap[plate.id] = countMap[plate.id]!! - 1
                }
                onCountChange?.onCountChange()
        }

        interface OnCountChange{
                fun onCountChange()
        }
}