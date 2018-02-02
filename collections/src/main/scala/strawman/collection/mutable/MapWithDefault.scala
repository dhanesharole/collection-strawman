package strawman.collection.mutable
import strawman.collection.MapFactory
import scala.{Option, Ordering, Unit}

trait MapWithDefault[K, V]
  extends Map[K, V]
  with MapOps[K, V, Map, MapWithDefault[K, V]]
  with WithDefault[K, V] {

  val defaultValue: K => V

  val underlying: Map[K, V]

  override def default(key: K): V = defaultValue(key)

  def iterator(): strawman.collection.Iterator[(K, V)] = underlying.iterator()

  def mapFactory: MapFactory[Map] = underlying.mapFactory

  def clear(): Unit = underlying.clear()

  def get(key: K): Option[V] = underlying.get(key)
}

final class MapWithDefaultImpl[K, V](val underlying: Map[K, V], val defaultValue: K => V) extends MapWithDefault[K, V] {

  def subtractOne(elem: K): MapWithDefaultImpl.this.type = { underlying.subtractOne(elem); this }

  def addOne(elem: (K, V)): MapWithDefaultImpl.this.type = { underlying.addOne(elem); this }

  def empty: MapWithDefault[K, V] = new MapWithDefaultImpl[K, V](underlying.empty, defaultValue)

  protected[this] def fromSpecificIterable(coll: strawman.collection.Iterable[(K, V)]): MapWithDefault[K, V] =
    new MapWithDefaultImpl[K, V](mapFactory.from(coll), defaultValue)

  protected[this] def newSpecificBuilder(): Builder[(K, V), MapWithDefault[K, V]] =
    Map.newBuilder().mapResult((p: Map[K, V]) => new MapWithDefaultImpl[K, V](p, defaultValue))

  protected[this] def mapFromIterable[K2, V2](it: strawman.collection.Iterable[(K2, V2)]): Map[K2, V2] =
      mapFactory.from(it)
}
