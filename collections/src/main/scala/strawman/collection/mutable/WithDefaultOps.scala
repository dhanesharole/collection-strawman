package strawman.collection.mutable

import strawman.collection.{IterableFactoryLike, MapFactory}
import scala.Option

private[mutable] trait WithDefaultOps[K, V, C <: Map[K, V]] { self: C =>
  
  val underlying: C

  def mapFactory: MapFactory[Map] = underlying.mapFactory
  
  def get(key: K): Option[V] = underlying.get(key)
  
  def iterator(): strawman.collection.Iterator[(K, V)] = underlying.iterator()

  override def iterableFactory: Iterable.type = underlying.iterableFactory

  override protected[this] def mapFromIterable[K2, V2](it: strawman.collection.Iterable[(K2, V2)]): Map[K2, V2] =
    underlying.mapFactory.from(it)
}
