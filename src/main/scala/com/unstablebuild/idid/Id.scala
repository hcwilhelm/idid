package com.unstablebuild.idid

trait Id {

  type UID

  def underlying: UID

  override final def equals(obj: scala.Any): Boolean = obj match {
    case that: Id if that.getClass == this.getClass => that.underlying == this.underlying
    case _ => false
  }

  override final def hashCode: Int = underlying.hashCode

  override final def toString: String = underlying.toString

}

object Id extends IdFunctions
