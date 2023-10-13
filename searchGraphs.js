// check if a node has any edges TO any other nodes
// if it is not pointing anywhere we dont want a dead end in our path
function deadEnd (row)   
{                       
    for (let i = 0; i < row.length; i++)
    {
        if (row[i] != 0) 
        {
            return false
        }
    }
    return true         
                        
}

// check if the start node is the target (we found a path), if true, add target to end of path, return path
// set the current node to visitied
// make sure the current node is not a dead end (i.e. all entries in row are 0), if it is, we dont want it in our path
// if the current node is not a dead, we add the current node to the path
// iterate through each row/node in the matrix/graph
// if we havent visited this node AND there exists a edge
    // make recursive call 
    // check if the resulted path is a valid path (i.e. final node in path is the target node)
// if there is no path we just return the empty path
function search(graph, start, target, visited, path)
{
    if (start == target)
    {
        path.push(target)
        return path
    }
    visited[start] = true
    if (!deadEnd(graph[start]))
    {
        path.push(start)
    }
    for (let i = 0; i < graph.length; i++)
    {
        if (visited[i] == false && graph[start][i] == 1)
        {
            let thispath = search(graph, i, target, visited, path)
            if (thispath[thispath.length-1] == target)
            {
                return thispath
            }
        }
    }
    return []
}

// initalize a 'visted' array of size V, and set all elements to false 
// create our empty 'path' array, to hold the path from start to destination
// make the call to our recursive function
// if the final element in the path is not our destination, return the empy path
// if the final element in the path is our destination, return the path
function depthFirstSearch(graph, startNode, targetNode)
{
    let visited = Array(graph.length).fill(false)
    let path = []
    search(graph, startNode, targetNode, visited, path)
    if (path[path.length-1] != targetNode)
    {
        console.log("FOUND ( startNode : " + startNode + ", targetNode : " + targetNode + " ) PATH NOT FOUND" ) 
        return []
    }
    else 
    {
        console.log("FOUND ( startNode : " + startNode + ", targetNode : " + targetNode + " ) path '" + path + "'")
        return path
    }
}


// matrices for teseting 
var adjMatrix2 = [
    [ 0, 0, 1, 0],
    [ 0, 0, 0, 1],
    [ 0, 0, 0, 1],
    [ 0, 1, 1, 0]]

var matrix = [
    [0,0,1,1,0,0],
    [0,0,0,0,0,0],
    [1,0,0,0,1,0],
    [0,0,0,0,1,0],
    [0,1,1,0,0,0],
    [0,0,0,1,0,0]
]

// testing calls 
let visited = Array(matrix.length).fill(false)
let x = search(matrix, 1, 1000, visited, []);
console.log(x)
depthFirstSearch(matrix, 0, 1)
