package payments.config

import pureconfig._
import pureconfig.generic.auto._

case class ServiceConf(api: ApiConf, http: ServerConf)
