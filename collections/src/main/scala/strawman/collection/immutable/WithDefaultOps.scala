package strawman.collection.immutable

import strawman.collection.{IterableFactoryLike, MapFactory}
import scala.{Option}

private[immutable] trait WithDefaultOps[K, +V, +C <: Map[K, V]] { self: C =>

  val underlying: C

  val defaultValue: K => V

  override def default(key: K): V = defaultValue(key)
}
