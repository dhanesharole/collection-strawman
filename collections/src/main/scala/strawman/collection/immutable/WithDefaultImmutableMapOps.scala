package strawman.collection.immutable

import strawman.collection.{IterableFactoryLike, MapFactory}
import scala.{Option}

trait WithDefaultImmutableMapOps[K, +V, +C <: Map[K, V]] { self: C =>
  
  val underlying: C
  
  val defaultValue: K => V
  
  def mapFactory: MapFactory[Map] = underlying.mapFactory
  
  def get(key: K): Option[V] = underlying.get(key)
  
  def iterableFactory: IterableFactoryLike[Iterable] = underlying.iterableFactory
  
  def iterator(): strawman.collection.Iterator[(K, V)] = underlying.iterator()
  
  override protected[this] def mapFromIterable[K2, V2](it: strawman.collection.Iterable[(K2, V2)]): Map[K2, V2] =
    underlying.mapFactory.from(it)
  
  override def default(key: K): V = defaultValue(key)
}
