package strawman.collection.mutable
import strawman.collection.{MapFactory, SortedMapFactory}
import scala.{Option, Ordering}

trait SortedMapWithDefault[K, V]
  extends SortedMap[K, V]
    with SortedMapOps[K, V, SortedMap, SortedMapWithDefault[K, V]]
    with MapWithDefault[K, V]{

  val underlying: SortedMap[K, V]

  def sortedMapFactory: SortedMapFactory[SortedMap] = underlying.sortedMapFactory

  def iteratorFrom(start: K): strawman.collection.Iterator[(K, V)] = underlying.iteratorFrom(start)

  def keysIteratorFrom(start: K): strawman.collection.Iterator[K] = underlying.keysIteratorFrom(start)

  implicit def ordering: Ordering[K] = underlying.ordering

  protected[this] def sortedMapFromIterable[K2, V2](it: strawman.collection.Iterable[(K2, V2)])(implicit ordering: Ordering[K2]): SortedMap[K2, V2] =
    sortedMapFactory.from(it)
}

final class SortedMapWithDefaultImpl[K, V](val underlying: SortedMap[K, V], val defaultValue: K => V) extends SortedMapWithDefault[K, V] {

  def subtractOne(elem: K): SortedMapWithDefaultImpl.this.type = { underlying.subtractOne(elem); this }

  def addOne(elem: (K, V)): SortedMapWithDefaultImpl.this.type = { underlying.addOne(elem); this }

  def empty: SortedMapWithDefault[K, V] = new SortedMapWithDefaultImpl[K, V](underlying.empty, defaultValue)

  def rangeImpl(from: Option[K], until: Option[K]): SortedMapWithDefault[K, V] =
    new SortedMapWithDefaultImpl[K, V](underlying.rangeImpl(from, until), defaultValue)

  protected[this] def fromSpecificIterable(coll: strawman.collection.Iterable[(K, V)]): SortedMapWithDefault[K, V] =
  new SortedMapWithDefaultImpl[K, V](sortedMapFactory.from(coll), defaultValue)

  protected[this] def newSpecificBuilder(): Builder[(K, V), SortedMapWithDefault[K, V]] =
    SortedMap.newBuilder().mapResult((p: SortedMap[K, V]) => new SortedMapWithDefaultImpl[K, V](p, defaultValue))
}