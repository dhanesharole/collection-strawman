package strawman.collection.immutable
import strawman.collection.{IterableFactoryLike, MapFactory, SortedMapFactory}
import strawman.collection.mutable.Builder
import scala.{Option, Ordering}

trait SortedMapWithDefault[K, +V]
  extends SortedMap[K, V]
    with SortedMapOps[K, V, SortedMap, SortedMapWithDefault[K, V]]
    with MapWithDefault[K, V] {

  val underlying: SortedMap[K, V]

  def sortedMapFactory: SortedMapFactory[SortedMap] = underlying.sortedMapFactory

  def iteratorFrom(start: K): strawman.collection.Iterator[(K, V)] = underlying.iteratorFrom(start)

  protected[this] def sortedMapFromIterable[K2, V2](it: strawman.collection.Iterable[(K2, V2)])(implicit ordering: Ordering[K2]): SortedMap[K2, V2] =
    underlying.sortedMapFactory.from(it)

  def keysIteratorFrom(start: K): strawman.collection.Iterator[K] = underlying.keysIteratorFrom(start)

}

final class SortedMapWithDefaultImpl[K, +V](val underlying: SortedMap[K, V], val defaultValue: K => V) extends SortedMapWithDefault[K, V] {

  implicit def ordering: Ordering[K] = underlying.ordering

  def updated[V1 >: V](key: K, value: V1): SortedMapWithDefault[K, V1] =
    new SortedMapWithDefaultImpl[K, V1](underlying.updated(key, value), defaultValue)

  def remove(key: K): SortedMapWithDefault[K, V] =
    new SortedMapWithDefaultImpl[K, V](underlying.remove(key), defaultValue)

  def empty: SortedMapWithDefaultImpl[K, V] = new SortedMapWithDefaultImpl[K, V](underlying.empty, defaultValue)

  def rangeImpl(from: Option[K], until: Option[K]): SortedMapWithDefault[K, V] =
    new SortedMapWithDefaultImpl[K, V](underlying.rangeImpl(from, until), defaultValue)

  override protected[this] def fromSpecificIterable(coll: strawman.collection.Iterable[(K, V)]): SortedMapWithDefaultImpl[K, V] =
    new SortedMapWithDefaultImpl[K, V](SortedMap.from(coll), defaultValue)

  override protected[this] def newSpecificBuilder(): Builder[(K, V), SortedMapWithDefault[K, V]] =
    SortedMap.newBuilder().mapResult((p: SortedMap[K, V]) => new SortedMapWithDefaultImpl[K, V](p, defaultValue))

}

