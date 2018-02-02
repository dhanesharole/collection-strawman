package strawman.collection.mutable

import strawman.collection.{IterableFactoryLike, MapFactory}

import scala.{ Option, Unit }

private[mutable] trait WithDefault[K, V]
private[mutable] trait WithDefaultOps[K, V] {

  /** The same map with a given default function.
    *  Note: The default is only used for `apply`. Other methods like `get`, `contains`, `iterator`, `keys`, etc.
    *  are not affected by `withDefault`.
    *
    *  Invoking transformer methods (e.g. `map`) will not preserve the default value.
    *
    *  @param d     the function mapping keys to values, used for non-present keys
    *  @return      a wrapper of the map with a default value
    */
  def withDefault(d: K => V): WithDefault[K, V]

  /** The same map with a given default value.
    *  Note: The default is only used for `apply`. Other methods like `get`, `contains`, `iterator`, `keys`, etc.
    *  are not affected by `withDefaultValue`.
    *
    *  Invoking transformer methods (e.g. `map`) will not preserve the default value.
    *
    *  @param d     default value used for non-present keys
    *  @return      a wrapper of the map with a default value
    */
  def withDefaultValue(d: V): WithDefault[K, V]
}