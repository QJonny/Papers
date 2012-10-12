package controllers

import play.api._
import play.api.mvc._
import java.io.File
import paper.Analyze
import session._

object Application extends Controller {
  val filesPath = "files/"
  
  
  def index = Action {
	def importPapers(): List[String] = {
		val filesDir = new File(filesPath)
		val files = filesDir.listFiles.toList
		
		files.map((f: File) => f.getName)
	}
  
	val papers = importPapers
	
	SessionManager.add("papers", papers)
	
    Ok(views.html.index(papers))
  }
  
  def upload = Action {request =>
	val body = request.body.asMultipartFormData
	if(body != None) {
		val file = body.get.file("paper")
		
		if(file != None) {
			val newFile = new File(filesPath + file.get.filename)
			if(!newFile.exists) file.get.ref.moveTo(newFile)
		}
	}
	
	Redirect(routes.Application.index)
  }
  
  def applyProcessing(paper: String, option: String) = {
	val params = List(filesPath + paper, option).toArray[String]
	Analyze.main(params)
  }
  
  def parse(paper: String) = Action {
	applyProcessing(paper, "-p")
	val papers = SessionManager.get[List[String]]("papers")
	Ok(papers.head)
	
	//Redirect(routes.Application.index)
  }
  
  def schedule(paper: String) = Action {
	applyProcessing(paper, "-s")
	Redirect(routes.Application.index)
  }
  
  def extend(paper: String) = Action {
	applyProcessing(paper, "-e")
	Redirect(routes.Application.index)
  }
  
	
  def comparePapers = Action {
	applyProcessing("", "-c")
	Redirect(routes.Application.index)
  }
  
  def createGraph = Action {
	applyProcessing("", "-g")
	Redirect(routes.Application.index)
  }
  
}