package strawman.collection.immutable

import strawman.collection.{IterableFactoryLike, MapFactory}
import scala.{Option}

private[immutable] trait MapWithDefaultOps[K, +V, CC[X, Y]] { self: Map[K, V] =>

  val defaultValue: K => V

  override def default(key: K): V = defaultValue(key)

  /** The same map with a given default function.
    *  Note: The default is only used for `apply`. Other methods like `get`, `contains`, `iterator`, `keys`, etc.
    *  are not affected by `withDefault`.
    *
    *  Invoking transformer methods (e.g. `map`) will not preserve the default value.
    *
    *  @param d     the function mapping keys to values, used for non-present keys
    *  @return      a wrapper of the map with a default value
    */
  def withDefault[V1 >: V](d: K => V1): CC[K, V1]

  /** The same map with a given default value.
    *  Note: The default is only used for `apply`. Other methods like `get`, `contains`, `iterator`, `keys`, etc.
    *  are not affected by `withDefaultValue`.
    *
    *  Invoking transformer methods (e.g. `map`) will not preserve the default value.
    *
    *  @param d     default value used for non-present keys
    *  @return      a wrapper of the map with a default value
    */
  def withDefaultValue[V1 >: V](d: V1): CC[K, V1]
}
