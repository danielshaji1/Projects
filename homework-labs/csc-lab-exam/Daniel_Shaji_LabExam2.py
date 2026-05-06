class UndirectGraph:
    def __init__(self):
        self.adjacency_list = {}
    def add_node(self, u):
        if u not in self.adjacency_list:
            self.adjacency_list[u] = []

    def add_edge(self, u, v):
        if u not in self.adjacency_list:
            self.add_node(u)
        if v not in self.adjacency_list:
            self.add_node(v)
        
        if not self.edge_exists(u, v):
            self.adjacency_list[u] += [v]
            self.adjacency_list[v] += [u]
        
    def node_exists(self, u):
        if u in self.adjacency_list:
            return True
        return False
    
    def edge_exists(self, u, v):
        if (v in self.adjacency_list[u]) and (u in self.adjacency_list[v]):
            return True
        return False
    
    def get_neighbors(self, u):
        return self.adjacency_list.get(u)
    
    def get_edge_density(self):
        node_count = len(self.adjacency_list) 
        actual_edges = sum(len(neighbors) for neighbors in self.adjacency_list.values()) // 2  # Total number of edges
        total_possible_edges = (node_count * (node_count - 1)) // 2

        if total_possible_edges == 0:
            return 0
        return actual_edges / total_possible_edges
    
    def is_complete(self):
        node_count = len(self.adjacency_list)
        if node_count < 2:
            return True

        for node, neighbors in self.adjacency_list.items():
            # The node should have exactly node_count - 1 neighbors (since it's undirected)
            if len(neighbors) != node_count - 1:
                return False
        return True


#Test
graph = UndirectGraph()
graph.add_edge(0, 1)
graph.add_edge(0, 2)
graph.add_edge(1, 2)
graph.add_edge(2, 3)

                            #Expected Output
print(graph.node_exists(1)) # True 
print(graph.node_exists(10)) # False

print(graph.edge_exists(1, 2)) # True
print(graph.edge_exists(1,3)) # False
#-----

print(graph.get_neighbors(1)) # [0, 2]

print(graph.get_edge_density()) # 0.67

print(graph.is_complete()) # False