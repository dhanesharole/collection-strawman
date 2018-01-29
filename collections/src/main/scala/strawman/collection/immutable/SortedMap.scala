package strawman
package collection
package immutable

import strawman.collection.mutable.Builder
import scala.{Option, Ordering, `inline`}
import scala.Predef.println

trait SortedMap[K, +V]
  extends Map[K, V]
    with collection.SortedMap[K, V]
    with SortedMapOps[K, V, SortedMap, SortedMap[K, V]] {

  /** The same sorted map with a given default function.
    *  Note: The default is only used for `apply`. Other methods like `get`, `contains`, `iterator`, `keys`, etc.
    *  are not affected by `withDefault`.
    *
    *  Invoking transformer methods (e.g. `map`) will not preserve the default value.
    *
    *  @param d     the function mapping keys to values, used for non-present keys
    *  @return      a wrapper of the map with a default value
    */
  override def withDefault[V1 >: V](d: K => V1): SortedMap[K, V1] = new SortedMap.WithDefault[K, V1](this, d)

  /** The same map with a given default value.
    * Note: The default is only used for `apply`. Other methods like `get`, `contains`, `iterator`, `keys`, etc.
    * are not affected by `withDefaultValue`.
    *
    * Invoking transformer methods (e.g. `map`) will not preserve the default value.
    *
    * @param d default value used for non-present keys
    * @return a wrapper of the map with a default value
    */
  override def withDefaultValue[V1 >: V](d: V1): SortedMap[K, V1] = new SortedMap.WithDefault[K, V1](this, _ => d)
}

trait SortedMapOps[K, +V, +CC[X, +Y] <: Map[X, Y] with SortedMapOps[X, Y, CC, _], +C <: SortedMapOps[K, V, CC, C]]
  extends MapOps[K, V, Map, C]
     with collection.SortedMapOps[K, V, CC, C] { self =>
    protected[this] def coll: C with CC[K, V]

    override def keySet: SortedSet[K] = new ImmutableKeySortedSet

    /** The implementation class of the set returned by `keySet` */
    protected class ImmutableKeySortedSet extends SortedSet[K] with GenKeySet with GenKeySortedSet {
      def iterableFactory: IterableFactory[Set] = Set
      def sortedIterableFactory: SortedIterableFactory[SortedSet] = SortedSet
      protected[this] def sortedFromIterable[B: Ordering](it: collection.Iterable[B]): SortedSet[B] = sortedIterableFactory.from(it)
      protected[this] def fromSpecificIterable(coll: collection.Iterable[K]): SortedSet[K] = sortedIterableFactory.from(coll)
      protected[this] def newSpecificBuilder(): Builder[K, SortedSet[K]] = sortedIterableFactory.newBuilder()
      def rangeImpl(from: Option[K], until: Option[K]): SortedSet[K] = {
        val map = self.rangeImpl(from, until)
        new map.ImmutableKeySortedSet
      }
      def empty: SortedSet[K] = sortedIterableFactory.empty
      def incl(elem: K): SortedSet[K] = fromSpecificIterable(this).incl(elem)
      def excl(elem: K): SortedSet[K] = fromSpecificIterable(this).excl(elem)
    }

    protected def mapFromIterable[K2, V2](it: collection.Iterable[(K2, V2)]): Map[K2, V2] =
      Map.from(it)

    // We override these methods to fix their return type (which would be `Map` otherwise)
    def updated[V1 >: V](key: K, value: V1): CC[K, V1]
    @`inline` final override def +[V1 >: V](kv: (K, V1)): CC[K, V1] = updated(kv._1, kv._2)

    override def concat[V2 >: V](xs: collection.Iterable[(K, V2)]): CC[K, V2] = {
        var result: CC[K, V2] = coll
        val it = xs.iterator()
        while (it.hasNext) result = result + it.next()
        result
    }

}

object SortedMap extends SortedMapFactory.Delegate[SortedMap](TreeMap) {

  final class WithDefault[K, +V](underlying: SortedMap[K, V], d: K => V) extends SortedMap[K, V] {
    override implicit def ordering: Ordering[K] = underlying.ordering

    override def updated[V1 >: V](key: K, value: V1): SortedMap[K, V1] = new WithDefault[K, V1](underlying.updated[V1](key, value), d)

    override def default(key: K): V = d(key)

    override def mapFactory: MapFactory[Map] = underlying.mapFactory

    override def get(key: K): Option[V] = underlying.get(key)

    override def empty: SortedMap[K, V] = new WithDefault[K, V](underlying.empty, d)

    override def iterator(): Iterator[(K, V)] = underlying.iterator()

    override def rangeImpl(from: Option[K], until: Option[K]): SortedMap[K, V] =
      new WithDefault[K, V](underlying.rangeImpl(from, until), d)

    override def remove(key: K): SortedMap[K, V] = new WithDefault[K, V](underlying - key, d)

    override def sortedMapFactory: SortedMapFactory[SortedMap] = underlying.sortedMapFactory

    override protected[this] def sortedMapFromIterable[K2, V2](it: collection.Iterable[(K2, V2)])(implicit ordering: Ordering[K2]): SortedMap[K2, V2] =
      SortedMap.from(it)

    override def iteratorFrom(start: K): Iterator[(K, V)] = underlying.iteratorFrom(start)

    override def keysIteratorFrom(start: K): Iterator[K] = underlying.keysIteratorFrom(start)

    override protected[this] def fromSpecificIterable(coll: collection.Iterable[(K, V)]): SortedMap[K, V] =
      new WithDefault[K, V](SortedMap.from(coll), d)

    override def iterableFactory: IterableFactoryLike[Iterable] = underlying.iterableFactory

    override protected[this] def newSpecificBuilder(): Builder[(K, V), SortedMap[K, V]] = {
      SortedMap.newBuilder[K, V]().mapResult(new WithDefault[K, V](_, d))
    }
  }
}