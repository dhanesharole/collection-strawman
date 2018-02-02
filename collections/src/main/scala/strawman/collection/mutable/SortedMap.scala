package strawman
package collection.mutable

import strawman.collection.SortedMapFactory
import scala.{Option, Ordering}

/**
  * Base type for mutable sorted map collections
  */
trait SortedMap[K, V]
  extends collection.SortedMap[K, V]
    with Map[K, V]
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
  override def withDefault(d: K => V): SortedMapWithDefault[K, V] = new SortedMapWithDefaultImpl[K, V](this, d)

  /** The same map with a given default value.
    * Note: The default is only used for `apply`. Other methods like `get`, `contains`, `iterator`, `keys`, etc.
    * are not affected by `withDefaultValue`.
    *
    * Invoking transformer methods (e.g. `map`) will not preserve the default value.
    *
    * @param d default value used for non-present keys
    * @return a wrapper of the map with a default value
    */
  override def withDefaultValue(d: V): SortedMapWithDefault[K, V] = new SortedMapWithDefaultImpl[K, V](this, _ => d)
}

trait SortedMapOps[K, V, +CC[X, Y] <: Map[X, Y] with SortedMapOps[X, Y, CC, _], +C <: SortedMapOps[K, V, CC, C]]
  extends collection.SortedMapOps[K, V, CC, C]
    with MapOps[K, V, Map, C] {

  def mapFromIterable[K2, V2](it: collection.Iterable[(K2, V2)]): Map[K2, V2] = Map.from(it)

}

object SortedMap extends SortedMapFactory.Delegate[SortedMap](TreeMap)