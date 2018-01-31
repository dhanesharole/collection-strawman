package strawman.collection.mutable

import strawman.collection.mutable.SortedMap.WithDefault
import strawman.collection.{IterableFactoryLike, MapFactory}

import scala.{ Option, Unit }

private[mutable] trait WithDefaultOps[K, V, C <: Map[K, V]] { self: C =>

  def subtractOne(elem: K): this.type = { underlying.subtractOne(elem); this }

  def addOne(elem: (K, V)): this.type = { underlying.addOne(elem); this}

  def clear(): Unit = underlying.clear()

  val underlying: C

  def mapFactory: MapFactory[Map] = underlying.mapFactory
  
  def get(key: K): Option[V] = underlying.get(key)
  
  def iterator(): strawman.collection.Iterator[(K, V)] = underlying.iterator()

  override def iterableFactory = underlying.iterableFactory

  override def mapFromIterable[K2, V2](it: strawman.collection.Iterable[(K2, V2)]): Map[K2, V2] =
    underlying.mapFactory.from(it)
}
