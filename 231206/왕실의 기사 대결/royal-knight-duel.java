import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
	static final int R[] = {-1, 0, 1, 0}, C[] = {0, 1, 0, -1};
	static int L, N, Q, map[][], live[][];
	static Player[] players;
	static Stack<Integer> stack;
	
	static class Player {
		int r, c, h, w, k, d;
		boolean out;
		
		Player(int r, int c, int h, int w, int k, int d, boolean out) {
			this.r = r; this.c = c;
			this.h = h; this.w = w;
			this.k = k; this.d = d;
			this.out = out;
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken()); N = Integer.parseInt(st.nextToken()); Q = Integer.parseInt(st.nextToken());
		map = new int[L][L]; live = new int[L][L];
		players = new Player[N];
		stack = new Stack<Integer>();
		
		for(int i=0; i<L; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<L; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
			Arrays.fill(live[i], -1);
		}
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			players[i] = new Player(Integer.parseInt(st.nextToken())-1, Integer.parseInt(st.nextToken())-1, Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), 0, false);
		}
		
		for(int n=0; n<N; n++) {
			Player p = players[n];
			for(int i=p.r; i<p.r+p.h; i++) {
				for(int j=p.c; j<p.c+p.w; j++) {
					live[i][j] = n;
				}
			}
		}
		
		while(Q-- > 0) {
			st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken())-1, d = Integer.parseInt(st.nextToken());
			
			if(players[n].out || !check(n, d))	continue;
			
			moveOthers(d);
			moveFirst(n, d);
		}
		
		int answer = 0;
		for(Player p : players) {
			if(!p.out)	answer += p.d;
		}
		System.out.print(answer);
	}

	private static void moveFirst(int n, int d) {
		Player p = players[n];
		
		for(int i=p.r; i<p.r+p.h; i++) {
			for(int j=p.c; j<p.c+p.w; j++) {
				live[i][j] = -1;
			}
		}
		
		p.r += R[d]; p.c += C[d];
		
		for(int i=p.r; i<p.r+p.h; i++) {
			for(int j=p.c; j<p.c+p.w; j++) {
				live[i][j] = n;
			}
		}
	}

	private static void moveOthers(int d) {
		while(!stack.isEmpty()) {
			int n = stack.pop();
			Player p = players[n];
			
			for(int i=p.r; i<p.r+p.h; i++) {
				for(int j=p.c; j<p.c+p.w; j++) {
					int dr = i + R[d];
					int dc = j + C[d];

					if(map[dr][dc] == 1)	p.d++;
					live[i][j] = -1;
				}
			}
			
			if(p.d >= p.k)	p.out = true;
			else {
				p.r += R[d]; p.c += C[d];
				
				for(int i=p.r; i<p.r+p.h; i++) {
					for(int j=p.c; j<p.c+p.w; j++) {
						live[i][j] = n;
					}
				}
			}
		}
	}

	private static boolean check(int n, int d) {
		Queue<Integer> queue = new LinkedList<Integer>();
		boolean visit[] = new boolean[N];
		queue.add(n);
		visit[n] = true;
		
		while(!queue.isEmpty()) {
			int idx = queue.poll();
			Player p = players[idx];
			
			for(int i=p.r; i<p.r+p.h; i++) {
				for(int j=p.c; j<p.c+p.w; j++) {
					int dr = i + R[d];
					int dc = j + C[d];
					
					if(dr < 0 || dr >= L || dc < 0 || dc >= L || map[dr][dc] == 2) {
						stack.clear();
						return false;
					}
					
					int nidx = live[dr][dc];
					if(nidx != -1 && !visit[nidx]) {
						visit[nidx] = true;
						stack.add(nidx);
						queue.add(nidx);
					}
				}
			}
		}
		
		return true;
	}
}