import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static final int r[] = {-1, 0, 1, 0, -1, -1, 1, 1}, c[] = {0, 1, 0, -1, -1, 1, -1, 1};
	static int N, M, P, C, D, map[][];
	static int rx, ry, K;
	static Santa santas[];
	static Queue<Integer> queue;
	
	static class Santa {
		int x, y, k, p; boolean d;
		Santa(int x, int y, int k, int p, boolean d) {
			this.x = x;
			this.y = y;
			this.k = k;
			this.p = p;
			this.d = d;
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); M = Integer.parseInt(st.nextToken()); P = Integer.parseInt(st.nextToken()); C = Integer.parseInt(st.nextToken()); D = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		st = new StringTokenizer(br.readLine());
		rx = Integer.parseInt(st.nextToken())-1; ry = Integer.parseInt(st.nextToken())-1;
		santas = new Santa[P];
		queue = new LinkedList<Integer>();
		
		for(int i=0; i<N; i++) {
			Arrays.fill(map[i], -1);
		}
		
		for(int i=0; i<P; i++) {
			st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken())-1, x = Integer.parseInt(st.nextToken())-1, y = Integer.parseInt(st.nextToken())-1;
			map[x][y] = n;
			santas[n] = new Santa(x, y, 0, 0, false);
			queue.add(i);
		}
		
		while(++K <= M) {
			if(queue.isEmpty())	break;
			
			moveRudolf();
			moveSanta();
			getPoint();
		}
		
		for(Santa s : santas) {
			sb.append(s.p).append(' ');
		}
		System.out.print(sb);
	}

	private static void moveRudolf() {
		int size = queue.size(), min = Integer.MAX_VALUE, dir = 0, sx = 0, sy = 0;
		
		while(size-- > 0) {
			int n = queue.poll();
			Santa santa = santas[n];
			int dist = (int) (Math.pow(santa.x - rx, 2) + Math.pow(santa.y - ry, 2)); 
			
			if(dist < min) {
				min = dist;
				sx = santa.x;
				sy = santa.y;
			} else if(dist == min) {
				if(sx < santa.x || (sx == santa.x && sy < santa.y)) {
					sx = santa.x;
					sy = santa.y;
				}
			}
			
			queue.add(n);
		}
		
		for(int d=0; d<8; d++) {
			int dx = rx + r[d];
			int dy = ry + c[d];
			if(dx >= 0 && dx < N && dy >= 0 && dy < N) {
				int dist = (int) (Math.pow(sx - dx, 2) + Math.pow(sy - dy, 2));
				if(dist < min) {
					min = dist;
					dir = d;
				}
			}
		}
		
		rx += r[dir];
		ry += c[dir];
		
		if(map[rx][ry] != -1) {
			int n = map[rx][ry];
			pushSanta(rx+r[dir]*C, ry+c[dir]*C, dir, n);
			santas[n].k = K-3;
			santas[n].p += C;
			map[rx][ry] = -1;
		}
	}
	
	private static void pushSanta(int x, int y, int d, int n) {
		if(x < 0 || x >= N || y < 0 || y >= N)	santas[n].d = true;
		else {
			if(map[x][y] != -1)	pushSanta(x+r[d], y+c[d], d, map[x][y]);
			
			santas[n].x = x;
			santas[n].y = y;
			map[x][y] = n;
		}
	}

	private static void moveSanta() {
		int size = queue.size();
		
		while(size-- > 0) {
			int n = queue.poll();
			Santa santa = santas[n];
			if(santa.d)	continue;
			
			if(++santa.k != K)	santa.k++;
			else {
				int min = (int) (Math.pow(santa.x - rx, 2) + Math.pow(santa.y - ry, 2)), dir = -1;
				for(int d=0; d<4; d++) {
					int sx = santa.x + r[d];
					int sy = santa.y + c[d];
					if(sx >= 0 && sx < N && sy >= 0 && sy < N && map[sx][sy] == -1) {
						int dist = (int) (Math.pow(sx - rx, 2) + Math.pow(sy - ry, 2));
						if(dist < min) {
							min = dist;
							dir = d;
						}
					}
				}
				
				if(dir != -1) {
					map[santa.x][santa.y] = -1;
					santa.x += r[dir];
					santa.y += c[dir];
					
					if(santa.x == rx && santa.y == ry) {
						dir = (dir+2)%4;
						pushSanta(santa.x + r[dir]*D, santa.y + c[dir]*D, dir, n);
						santas[n].p += D;
						santas[n].k = K-1;
					} else {
						map[santa.x][santa.y] = n;
					}
				}
			}
			
			if(!santa.d)	queue.add(n);
		}
	}
	
	private static void getPoint() {
		int size = queue.size();
		while(size-- > 0) {
			int n = queue.poll();
			if(santas[n].d)	continue;
			santas[n].p++;
			queue.add(n);
		}
	}
}