import networkx as nx

graph = nx.read_edgelist("/Users/qixiongliu/Desktop/Graduate/CSCI572/HW/HW4/Answer/edgesList.txt", create_using=nx.DiGraph())
# print graph.nodes()

pr = nx.pagerank(graph, alpha=0.85, personalization=None, max_iter=100, tol=1.0e-6, nstart=None, weight='weight', dangling=None)

outputFile = "/Users/qixiongliu/Desktop/Graduate/CSCI572/HW/HW4/Answer/external_pageRankFile.txt"
f = open(outputFile, "w")

idPrefix = "/Users/qixiongliu/Desktop/Graduate/CSCI572/HW/HW4/DataSet/NBC_News/crawl_data/"
for id in pr:
	# print idPrefix + id + "=" + str(pr[id])
	f.write(idPrefix + id + "=" + str(pr[id]) + "\n")

f.close()