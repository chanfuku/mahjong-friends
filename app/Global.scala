import play.api.mvc.WithFilters
import play.api.{Application, GlobalSettings}

object Global extends WithFilters() with GlobalSettings {

  override def onStart(app: Application) {
    super.onStart(app)

    skinny.DBSettings.initialize()
  }
}