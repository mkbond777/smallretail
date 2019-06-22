package filters

import com.google.inject.Inject
import play.api.http.HttpFilters
import play.filters.cors.CORSFilter

// TODO Need to understand this filters and its usage

class Filters @Inject()(corsFilter: CORSFilter) extends HttpFilters {
  def filters: Seq[CORSFilter] = Seq(corsFilter)
}
