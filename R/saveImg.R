## ggplot options
fontTitleSize <- 21
fontAxisTitleSize <- 18
fontGeomTextSize <- 10

getGGTheme <- function() {
  return (theme(plot.title=element_text(size=fontTitleSize),
                axis.title=element_text(size=fontAxisTitleSize)))
  #return (theme())
}



w <- 800
h <-  450
imgPath <- "../pictures/"

openImg <- function(fName) {
  png(filename=paste0(imgPath, fName), width=w, height=h)
}



closeImg <- function() {
  dev.off()
}


  #ggsave(g, filename=fName, path=imgPath, width=w, height=h, )