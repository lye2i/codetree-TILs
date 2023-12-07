import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static final int R[] = {-1, 0, 1, 0}, C[] = {0, 1, 0, -1};
	static int N, M, answer, map[][], visit[][];
	
	static class Bomb implements Comparable<Bomb>{
		int n, r, c;
		
		Bomb(int n, int r, int c) {
			this.n = n;
			this.r = r;
			this.c = c;
		}
		
		@Override
		public int compareTo(Bomb b) {
			if(this.n != b.n)	return b.n - this.n;
			else if(this.r != b.r)	return b.r - this.r;
			else	return this.c - b.c;
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); M = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		while(true) {
			Bomb bomb = findBomb();
			if(bomb == null || bomb.n < 2)	break;
			else	explode(bomb);
			gravity();
			rotation();
			gravity();
		}
		
		System.out.print(answer);
	}
	
	private static Bomb findBomb() {
		PriorityQueue<Bomb> pq = new PriorityQueue<Bomb>();
		visit = new int[N][N];
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				if(map[i][j] > 0 && visit[i][j] == 0) {
					visit[i][j] = map[i][j];
					pq.add(getBomb(map[i][j], i, j));
				}
			}
		}
		
		return pq.isEmpty() ? null : pq.poll();
	}

	private static Bomb getBomb(int m, int i, int j) {
		Queue<int[]> queue = new LinkedList<int[]>();
		Bomb bomb = new Bomb(1, i, j);
		queue.add(new int[] {i, j});
		
		while(!queue.isEmpty()) {
			int p[] = queue.poll();
			
			for(int d=0; d<4; d++) {
				int dr = p[0] + R[d];
				int dc = p[1] + C[d];
				if(dr < 0 || dr >= N || dc < 0 || dc >= N || map[dr][dc] < 0 || visit[dr][dc] == m)	continue;
				
				if(map[dr][dc] == 0 || map[dr][dc] == m) {
					visit[dr][dc] = m;
					bomb.n++;
					queue.add(new int[] {dr, dc});
					
					if(map[dr][dc] == m) {
						if(bomb.r < dr || (bomb.r == dr && bomb.c > dc)) {
							bomb.r = dr;
							bomb.c = dc;
						}
					}
				}
			}
		}

		return bomb;
	}

	private static void explode(Bomb bomb) {
		Queue<int[]> queue = new LinkedList<int[]>();
		boolean visited[][] = new boolean[N][N];
		int idx = map[bomb.r][bomb.c];
		queue.add(new int[] {bomb.r, bomb.c});
		visited[bomb.r][bomb.c] = true;
		map[bomb.r][bomb.c] = -2;
		
		while(!queue.isEmpty()) {
			int p[] = queue.poll();
			
			for(int d=0; d<4; d++) {
				int dr = p[0] + R[d];
				int dc = p[1] + C[d];
				if(dr < 0 || dr >= N || dc < 0 || dc >= N || map[dr][dc] < 0 || visited[dr][dc])	continue;
				
				if(map[dr][dc] == 0 || map[dr][dc] == idx) {
					visited[dr][dc] = true;
					map[dr][dc] = -2;
					queue.add(new int[] {dr, dc});
				}
			}
		}

		answer += (bomb.n * bomb.n);
	}
	
	private static void gravity() {
		for(int i=0; i<N; i++) {
			int cnt = 0;
			for(int j=N-1; j>=0; j--) {
				if(map[j][i] == -2)	cnt++;
				else if(map[j][i] == -1)	cnt = 0;
				else if(cnt > 0) {
					map[j+cnt][i] = map[j][i];
					map[j][i] = -2;
				} 
			}
		}
	}

	private static void rotation() {
		int tmp[][] = new int[N][N];
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				tmp[i][j] = map[j][N-i-1];
			}
		}
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				map[i][j] = tmp[i][j];
			}
		}
	}
}