package com.unstablebuild.idid

import com.unstablebuild.idid.factory.IdFactory
import com.unstablebuild.idid.source.IdSource

trait IdFunctions {
  def create[T <: Id](uid: T#UID)(implicit idFactory: IdFactory[T], idSource: IdSource[T#UID]): T = idFactory.create(uid)

  def empty[T <: Id](implicit idFactory: IdFactory[T], idSource: IdSource[T#UID]): T = idFactory.empty

  def parse[T <: Id](str: String)(implicit idFactory: IdFactory[T], idSource: IdSource[T#UID]): T = idFactory.parse(str)

  def random[T <: Id](implicit idFactory: IdFactory[T], idSource: IdSource[T#UID]): T = idFactory.random

  def value[T <: Id](id: T): T#UID = id.underlying
}
