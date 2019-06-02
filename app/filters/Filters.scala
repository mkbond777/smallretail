package filters

import com.google.inject.Inject
import play.api.http.HttpFilters
import play.filters.cors.CORSFilter

class Filters @Inject()(corsFilter: CORSFilter) extends HttpFilters {
  def filters = Seq(corsFilter)
}
