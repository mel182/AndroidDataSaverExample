package com.jetpackcompose.realmdb.models

import io.realm.kotlin.types.EmbeddedRealmObject

class Address: EmbeddedRealmObject { // normally you must inherit the 'RealmObject' but since in this case
                             // we want to embed the object into our teacher object we inherit 'EmbeddedRealmObject'
    //@PrimaryKey var _id: ObjectId = ObjectId() // -> you want a seperated primary key
    var fullName: String = ""
    var street: String = ""
    var houseNumber: Int = 0
    var zip: Int = 0
    var city: String = ""
    var teacher: Teacher? = null
}