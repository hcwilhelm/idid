package com.unstablebuild.idid

trait TypedId[T] extends Id {
  override type UID = T
}
