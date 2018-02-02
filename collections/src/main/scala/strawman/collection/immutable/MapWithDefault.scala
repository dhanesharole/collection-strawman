package strawman.collection.immutable

import strawman.collection
import strawman.collection.mutable.Builder
import strawman.collection.{IterableFactoryLike, Iterator, MapFactory}
import scala.{Option, Ordering}

trait MapWithDefault[K, +V]
  extends Map[K, V]
    with MapOps[K, V, Map, MapWithDefault[K, V]]
    with WithDefault[K, V] {

  val defaultValue: K => V

  val underlying: Map[K, V]

  def get(key: K): Option[V] = underlying.get(key)

  override def default(key: K): V = defaultValue(key)

  def iterableFactory: IterableFactoryLike[Iterable] = underlying.iterableFactory

  def iterator(): Iterator[(K, V)] = underlying.iterator()

  def mapFactory: MapFactory[Map] = underlying.mapFactory

}

final class MapWithDefaultImpl[K, +V](val underlying: Map[K, V], val defaultValue: K => V) extends MapWithDefault[K, V] {

  def remove(key: K): MapWithDefault[K, V] = new MapWithDefaultImpl[K, V](underlying.remove(key), defaultValue)

  def updated[V1 >: V](key: K, value: V1): MapWithDefault[K, V1] =
    new MapWithDefaultImpl[K, V1](underlying.updated(key, value), defaultValue)

  def empty: MapWithDefault[K, V] = new MapWithDefaultImpl[K, V](underlying.empty, defaultValue)

  protected[this] def mapFromIterable[K2, V2](it: collection.Iterable[(K2, V2)]): Map[K2, V2] =
    underlying.mapFactory.from(it)

  protected[this] def fromSpecificIterable(coll: collection.Iterable[(K, V)]): MapWithDefault[K, V] =
    new MapWithDefaultImpl[K, V](Map.from(coll), defaultValue)

  protected[this] def newSpecificBuilder(): Builder[(K, V), MapWithDefault[K, V]] =
    Map.newBuilder().mapResult((p: Map[K, V]) => new MapWithDefaultImpl[K, V](p, defaultValue))

}

