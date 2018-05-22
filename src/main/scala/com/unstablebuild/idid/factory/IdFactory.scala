package com.unstablebuild.idid.factory

import com.unstablebuild.idid.Id
import com.unstablebuild.idid.source.IdSource

trait IdFactory[ID <: Id] {
  def create(uid: ID#UID): ID

  def empty(implicit source: IdSource[ID#UID]): ID = create(source.empty)
  def parse(str: String)(implicit source: IdSource[ID#UID]): ID = create(source.parse(str))
  def random(implicit source: IdSource[ID#UID]): ID = create(source.random)
  def value(id: ID): ID#UID = id.underlying
}
