
function findUnmarked(dist, visited, n)
{
    let unmarked = Infinity
    for (let v = 0; v < n; v++)
    {
        if (visited[v] == false && dist[v] < Infinity)
        {
            unmarked = v
        }
    }
    return unmarked
}

// dist holds the distance from the sourceNode to all node in the graph
function _dijkstra(graph, sourceNode, visited, dist) 
{
    dist[sourceNode] = 0;
    let unmarked = sourceNode;
    while (unmarked != Infinity) 
    {
        unmarked = findUnmarked(dist, visited, graph.length);

        if (unmarked != Infinity) 
        {
            visited[unmarked] = true;
            
            for (let w = 0; w < graph.length; w++) 
            {
                if (w != unmarked && !visited[w] && graph[unmarked][w] > 0) 
                {
                    dist[w] = Math.min(dist[w], dist[unmarked] + graph[unmarked][w]);
                }
            }
        }
    }
    return dist;
}

// initalize distances to infinity 
// distance to sourceNode is zero of course
// intialize all visited entries to false
// call recursive function
function dijkstra(graph, sourceNode) 
{
    var dist = Array(graph.length).fill(Infinity) 
    let visited = Array(graph.length).fill(false) 

    let shortestPaths = _dijkstra(graph, sourceNode, visited, dist)
    
    console.log(shortestPaths)
}


// matrices for teseting 
var adjMatrix2 = [
    [ 0, 0, 5, 0],
    [ 0, 0, 0, 3],
    [ 0, 0, 0, 2],
    [ 0, 8, 10, 0]]

    var matrix = [
        [3,0,5,0,0,0],
        [0,3,0,2,0,10],
        [1,0,3,0,10,0],
        [0,2,3,0,8,0],
        [0,20,7,0,8,0],
        [1,0,5,0,0,2]
    ]

// testing calls 
dijkstra(matrix, 1)

