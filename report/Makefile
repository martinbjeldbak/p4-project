all: report.tex
	xelatex report.tex
	bibtex report
	xelatex report.tex
	xelatex report.tex > texoutput
	grep Warning -i --color -n texoutput

