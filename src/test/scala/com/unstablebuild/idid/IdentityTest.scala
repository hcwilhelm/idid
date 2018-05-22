package com.unstablebuild.idid

import java.util.UUID

import com.unstablebuild.idid.factory.IdFactory
import com.unstablebuild.idid.source.IdSource
import org.scalatest.{FlatSpec, MustMatchers}

class IdentityTest extends FlatSpec with MustMatchers {

  case class MyOwnId(underlying: UUID) extends TypedId[UUID]

  object MyOwnId {

    /**
      * We could use SAM abstraction here but we would not be able
      * to cross compile to scala 2.11 anymore
      */
    implicit val idFactory: IdFactory[MyOwnId] = new IdFactory[MyOwnId] {
      override def create(uid: UUID): MyOwnId = MyOwnId(uid)
    }
  }

  case class MyOtherId(underlying: UUID) extends TypedId[UUID]

  object MyOtherId {

    /**
      * We could use SAM abstraction here but we would not be able
      * to cross compile to scala 2.11 anymore
      */
    implicit val idFactory: IdFactory[MyOtherId] = new IdFactory[MyOtherId] {
      override def create(uid: UUID): MyOtherId = MyOtherId(uid)
    }
  }

  case class MyOtherStringId(underlying: String) extends TypedId[String]

  object MyOtherStringId {

    /**
      * We could use SAM abstraction here but we would not be able
      * to cross compile to scala 2.11 anymore
      */
    implicit val idFactory: IdFactory[MyOtherStringId] = new IdFactory[MyOtherStringId] {
      override def create(uid: String): MyOtherStringId = MyOtherStringId(uid)
    }
  }

  it must "allow creating ids" in {
    val uuid = UUID.randomUUID()

    val id = Id.create[MyOwnId](uuid)
    Id.value[MyOwnId](id) must equal (uuid)
  }

  it must "return the underlying id as the string representation" in {
    val uuid = UUID.randomUUID()

    Id.create[MyOwnId](uuid).toString must equal (uuid.toString)
  }

  it must "implements equals and hashCode" in {
    val id1 = Id.random[MyOwnId]
    val id2 = Id.parse[MyOwnId](id1.toString)

    id1 must equal (id2)
    id1.hashCode must equal (id2.hashCode)
  }

  it must "allow the original class to be used normally" in {
    val id1 = Id.random[MyOwnId]
    val id2 = MyOwnId(id1.underlying)

    id1 must equal (id2)
    id1.hashCode must equal (id2.hashCode)
  }

  it must "support multiple types with the same underlying id" in {


    val c1 = Id.random[MyOwnId]
    val c2 = Id.create[MyOtherId](Id.value[MyOwnId](c1))

    c1 must not equal c2
    c1.hashCode must equal (c2.hashCode)
  }

  it must "implement a base interface" in {

    val ids: Set[Id] = Set(Id.random[MyOwnId], Id.random[MyOtherStringId])

    ids.size must equal (2)
  }

  it must "allow custom functions to use the ids" in {

    val id = Id.random[MyOwnId]
    def parse[T <: Id](str: String)(implicit idFactory: IdFactory[T], idSource: IdSource[T#UID]): T = Id.parse[T](str)

    parse[MyOwnId](id.toString) must equal (id)
  }

}
